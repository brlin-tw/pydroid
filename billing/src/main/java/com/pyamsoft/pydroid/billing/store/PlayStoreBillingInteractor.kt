/*
 * Copyright 2024 pyamsoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.pydroid.billing.store

import android.app.Activity
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.CheckResult
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ConsumeResponseListener
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.ProductDetailsResponseListener
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.pyamsoft.pydroid.billing.BillingConnector
import com.pyamsoft.pydroid.billing.BillingInteractor
import com.pyamsoft.pydroid.billing.BillingLauncher
import com.pyamsoft.pydroid.billing.BillingSku
import com.pyamsoft.pydroid.billing.BillingState
import com.pyamsoft.pydroid.bus.EventBus
import com.pyamsoft.pydroid.core.Logger
import com.pyamsoft.pydroid.core.ThreadEnforcer
import com.pyamsoft.pydroid.core.cast
import com.pyamsoft.pydroid.util.doOnCreate
import com.pyamsoft.pydroid.util.doOnDestroy
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class PlayStoreBillingInteractor
internal constructor(
    context: Context,
    private val enforcer: ThreadEnforcer,
    private val errorBus: EventBus<Throwable>,
) :
    BillingConnector,
    BillingInteractor,
    BillingLauncher,
    BillingClientStateListener,
    PurchasesUpdatedListener,
    ConsumeResponseListener,
    ProductDetailsResponseListener {

  private val client by lazy {
      // Billing 7 change
      // https://developer.android.com/google/play/billing/release-notes#google_play_billing_library_700_release_2024-05-14
      val pendingPurchaseParams = PendingPurchasesParams.newBuilder().enableOneTimeProducts()
          .build()

    BillingClient.newBuilder(context.applicationContext)
        .setListener(this)
        .enablePendingPurchases(pendingPurchaseParams)
        .build()
  }

  private val appSkuList: List<String>

  private val skuFlow =
      MutableStateFlow(
          State(
              state = BillingState.LOADING,
              list = emptyList(),
          ),
      )

  private val billingScope = MainScope()

  private var backoffCount = 1

  init {
    Logger.d { "Construct new interactor and billing client" }

    val rawPackageName = context.applicationContext.packageName
    val packageName =
        if (rawPackageName.endsWith(DEV_SUFFIX))
            rawPackageName.substring(0 until rawPackageName.length - DEV_SUFFIX.length)
        else rawPackageName

    appSkuList =
        listOf(
            "$packageName.iap_one",
            "$packageName.iap_three",
            "$packageName.iap_five",
            "$packageName.iap_ten",
        )
  }

  private fun connect() {
    enforcer.assertOnMainThread()

    if (!client.isReady) {
      Logger.d { "Connect to Billing Client" }
      client.startConnection(this)
    }
  }

  private fun disconnect() {
    enforcer.assertOnMainThread()

    Logger.d { "Disconnect from billing client" }
    client.endConnection()

    billingScope.cancel()
  }

  private fun querySkus() {
    enforcer.assertOnMainThread()

    Logger.d { "Querying for SKUs $appSkuList" }

    // Map this here every time since we do not know if the QPDP builder carries state that cannot
    // be re-used.
    val skus =
        appSkuList.map { sku ->
          QueryProductDetailsParams.Product.newBuilder()
              .setProductType(BillingClient.ProductType.INAPP)
              .setProductId(sku)
              .build()
        }

    val params = QueryProductDetailsParams.newBuilder().setProductList(skus).build()

    client.queryProductDetailsAsync(params, this)
  }

  private fun handlePurchases(purchases: List<Purchase>) {
    enforcer.assertOnMainThread()

    for (purchase in purchases) {
      Logger.d { "Consume purchase: $purchase" }
      val params = ConsumeParams.newBuilder().setPurchaseToken(purchase.purchaseToken).build()
      client.consumeAsync(params, this)
    }
  }

  override fun onProductDetailsResponse(result: BillingResult, skuDetails: List<ProductDetails>) {
    if (result.isOk()) {
      Logger.d { "Sku response: $skuDetails" }
      val skuList = skuDetails.map { PlayBillingSku(it) }
      skuFlow.value = State(BillingState.CONNECTED, skuList)
    } else {
      Logger.w { "SKU response not OK: ${result.debugMessage}" }
      skuFlow.value = State(BillingState.DISCONNECTED, emptyList())
    }
  }

  override fun bind(activity: ComponentActivity) {
    activity.lifecycle.doOnCreate {
      Logger.d { "Attempt to connect Billing on Activity create" }
      connect()
    }

    activity.lifecycle.doOnDestroy {
      Logger.d { "Attempt disconnect Billing on Activity destroy" }
      disconnect()
    }
  }

  override fun onConsumeResponse(result: BillingResult, token: String) {
    if (result.isOk()) {
      Logger.d { "Purchase consumed $token" }
    } else {
      Logger.w { "Consume response not OK: ${result.debugMessage}" }
    }
  }

  override fun onBillingSetupFinished(result: BillingResult) {
    if (result.isOk()) {
      Logger.d { "Billing client is ready, query products!" }

      // Reset the backoff to 1
      backoffCount = 1

      querySkus()
    } else {
      Logger.w { "Billing setup not OK: ${result.debugMessage}" }
      skuFlow.value = State(BillingState.DISCONNECTED, emptyList())
    }
  }

  override fun onBillingServiceDisconnected() {
    Logger.w { "Billing client was disconnected!" }

    billingScope.launch(context = Dispatchers.Default) {
      val waitTime = backoffCount
      backoffCount *= 2

      if (backoffCount < 1024) {
        Logger.d { "Wait to reconnect for $waitTime seconds" }
        delay(waitTime.seconds)

        withContext(context = Dispatchers.Main) {
          Logger.d { "Try connecting again" }
          connect()
        }
      } else {
        Logger.w { "We have tried to connect and have been unsuccessful. Billing DISABLED" }
      }
    }
  }

  override suspend fun refresh() =
      withContext(context = Dispatchers.Main) {
        if (!client.isReady) {
          Logger.w { "Client is not ready yet, so we are not refreshing sku and purchases" }
          return@withContext
        }

        querySkus()
      }

  override fun watchSkuList(): Flow<BillingInteractor.BillingSkuListSnapshot> =
      skuFlow.map {
        BillingInteractor.BillingSkuListSnapshot(
            status = it.state,
            skus = it.list,
        )
      }

  override suspend fun purchase(activity: Activity, sku: BillingSku): Unit =
      withContext(context = Dispatchers.Default) {
        val realSku = sku.cast<PlayBillingSku>()
        if (realSku == null) {
          errorBus.emit(IllegalArgumentException("SKU must be of type PlayBillingSku"))
          return@withContext
        }

        try {
          val products =
              listOf(
                  BillingFlowParams.ProductDetailsParams.newBuilder()
                      .setProductDetails(realSku.sku)
                      // Do not need to set offerToken since we are not a subscription
                      .build(),
              )

          val params = BillingFlowParams.newBuilder().setProductDetailsParamsList(products).build()

          withContext(context = Dispatchers.Main) {
            Logger.d { "Launch purchase flow ${realSku.id}" }
            client.launchBillingFlow(activity, params)
          }
        } catch (e: Throwable) {
          Logger.e(e) { "Failed purchase flow for SKU: $realSku" }
          errorBus.emit(RuntimeException(e.message ?: "An error occurred during purchasing."))
        }
      }

  override fun watchBillingErrors(): Flow<Throwable> =
      errorBus.onEach { Logger.e(it) { "Billing error received!" } }

  override fun onPurchasesUpdated(result: BillingResult, purchases: List<Purchase>?) {
    if (result.isOk()) {
      if (purchases != null) {
        Logger.d { "Purchase succeeded! $purchases" }
        handlePurchases(purchases)
      } else {
        Logger.w { "Purchase list was null!" }
      }
    } else {
      if (result.isUserCancelled()) {
        Logger.d { "User has cancelled purchase flow." }
      } else {
        billingScope.launch(context = Dispatchers.Default) {
          Logger.w { "Purchase response not OK: ${result.debugMessage}" }
          errorBus.emit(RuntimeException(result.debugMessage))
        }
      }
    }
  }

  private data class State(
      val state: BillingState,
      val list: List<PlayBillingSku>,
  )

  companion object {

    private const val DEV_SUFFIX = ".dev"

    @JvmStatic
    @CheckResult
    private fun BillingResult.isUserCancelled(): Boolean {
      return this.responseCode == BillingClient.BillingResponseCode.USER_CANCELED
    }

    @JvmStatic
    @CheckResult
    private fun BillingResult.isOk(): Boolean {
      return this.responseCode == BillingClient.BillingResponseCode.OK
    }
  }
}
