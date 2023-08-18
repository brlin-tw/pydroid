/*
 * Copyright 2023 pyamsoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.pydroid.ui.internal.billing.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import com.pyamsoft.pydroid.billing.BillingSku
import com.pyamsoft.pydroid.billing.BillingState
import com.pyamsoft.pydroid.theme.keylines
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.haptics.LocalHapticManager
import com.pyamsoft.pydroid.ui.internal.app.AppHeaderDialog
import com.pyamsoft.pydroid.ui.internal.app.dialogItem
import com.pyamsoft.pydroid.ui.internal.test.createNewTestImageLoader
import com.pyamsoft.pydroid.ui.util.collectAsStateListWithLifecycle

@Composable
internal fun BillingScreen(
    modifier: Modifier = Modifier,
    state: BillingDialogViewState,
    imageLoader: ImageLoader,
    onPurchase: (BillingSku) -> Unit,
    onBillingErrorDismissed: () -> Unit,
    onClose: () -> Unit,
) {
  val skuList = state.skuList.collectAsStateListWithLifecycle()
  val connected by state.connected.collectAsStateWithLifecycle()
  val icon by state.icon.collectAsStateWithLifecycle()
  val name by state.name.collectAsStateWithLifecycle()
  val error by state.error.collectAsStateWithLifecycle()

  val hapticManager = LocalHapticManager.current
  val snackbarHostState = remember { SnackbarHostState() }

  // Remember computed value
  val isLoading = remember(connected) { connected == BillingState.LOADING }
  val isConnected = remember(connected) { connected == BillingState.CONNECTED }
  val isError =
      remember(
          isConnected,
          skuList,
      ) {
        skuList.isEmpty() || !isConnected
      }

  AppHeaderDialog(
      modifier = modifier,
      icon = icon,
      name = name,
      imageLoader = imageLoader,
  ) {
    if (isLoading) {
      dialogItem(
          modifier = Modifier.fillMaxWidth(),
      ) {
        Loading(
            modifier = Modifier.fillMaxWidth(),
        )
      }
    } else if (isError) {
      dialogItem(
          modifier = Modifier.fillMaxWidth(),
      ) {
        ErrorText(
            modifier = Modifier.fillMaxWidth(),
        )
      }
    } else {
      skuList.forEach { item ->
        dialogItem(
            modifier = Modifier.fillMaxWidth(),
        ) {
          BillingListItem(
              modifier = Modifier.fillMaxWidth(),
              sku = item,
              onPurchase = onPurchase,
          )
        }
      }
    }

    dialogItem(
        modifier = Modifier.fillMaxWidth(),
    ) {
      BillingError(
          modifier = Modifier.fillMaxWidth(),
          snackbarHostState = snackbarHostState,
          error = error,
          onSnackbarDismissed = onBillingErrorDismissed,
      )
    }

    dialogItem(
        modifier = Modifier.fillMaxWidth(),
    ) {
      Row(
          modifier =
              Modifier.padding(horizontal = MaterialTheme.keylines.content)
                  .padding(top = MaterialTheme.keylines.content),
      ) {
        Spacer(
            modifier = Modifier.weight(1F),
        )
        TextButton(
            onClick = {
              hapticManager?.cancelButtonPress()
              onClose()
            },
        ) {
          Text(
              text = stringResource(R.string.close),
          )
        }
      }
    }
  }
}

@Composable
private fun ErrorText(
    modifier: Modifier = Modifier,
) {
  Box(
      modifier = modifier.padding(MaterialTheme.keylines.content),
      contentAlignment = Alignment.Center,
  ) {
    Text(
        text = stringResource(R.string.billing_error_message),
        style = MaterialTheme.typography.body1)
  }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier,
) {
  Box(
      modifier = modifier.padding(MaterialTheme.keylines.content),
      contentAlignment = Alignment.Center,
  ) {
    CircularProgressIndicator()
  }
}

@Composable
private fun BillingError(
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,
    error: Throwable?,
    onSnackbarDismissed: () -> Unit,
) {
  SnackbarHost(
      modifier = modifier,
      hostState = snackbarHostState,
  )

  if (error != null) {
    LaunchedEffect(error) {
      val message = error.message
      snackbarHostState.showSnackbar(
          message = if (message.isNullOrBlank()) "An unexpected error occurred" else message,
          duration = SnackbarDuration.Short,
      )

      // We ignore the showSnackbar result because we don't care (no actions)
      onSnackbarDismissed()
    }
  }
}

private val PREVIEW_SKUS =
    mutableStateListOf(
        object : BillingSku {
          override val id: String = "test"
          override val displayPrice: String = "$10.00"
          override val price: Long = 1000
          override val title: String = "TEST"
          override val description: String = "JUST A TEST"
        },
        object : BillingSku {
          override val id: String = "test2"
          override val displayPrice: String = "$20.00"
          override val price: Long = 2000
          override val title: String = "TEST AGAIN"
          override val description: String = "JUST ANOTHER TEST"
        },
    )

@Composable
private fun PreviewBillingScreen(
    connected: BillingState,
    skuList: SnapshotStateList<BillingSku>,
    error: Throwable?,
) {
  BillingScreen(
      state =
          MutableBillingDialogViewState().apply {
            this.name.value = "TEST APPLICATION"
            this.connected.value = connected
            this.skuList.value = skuList
            this.error.value = error
          },
      imageLoader = createNewTestImageLoader(),
      onPurchase = {},
      onBillingErrorDismissed = {},
      onClose = {},
  )
}

@Preview
@Composable
private fun PreviewBillingScreenConnectedWithNoListNoError() {
  PreviewBillingScreen(
      connected = BillingState.CONNECTED,
      skuList = remember { mutableStateListOf() },
      error = null,
  )
}

@Preview
@Composable
private fun PreviewBillingScreenConnectedWithNoListError() {
  PreviewBillingScreen(
      connected = BillingState.CONNECTED,
      skuList = remember { mutableStateListOf() },
      error = RuntimeException("TEST"),
  )
}

@Preview
@Composable
private fun PreviewBillingScreenConnectedWithListError() {
  PreviewBillingScreen(
      connected = BillingState.CONNECTED,
      skuList = PREVIEW_SKUS,
      error = RuntimeException("TEST"),
  )
}

@Preview
@Composable
private fun PreviewBillingScreenConnectedWithListNoError() {
  PreviewBillingScreen(
      connected = BillingState.CONNECTED,
      skuList = PREVIEW_SKUS,
      error = null,
  )
}

@Preview
@Composable
private fun PreviewBillingScreenDisconnectedWithListError() {
  PreviewBillingScreen(
      connected = BillingState.DISCONNECTED,
      skuList = PREVIEW_SKUS,
      error = RuntimeException("TEST"),
  )
}

@Preview
@Composable
private fun PreviewBillingScreenDisconnectedWithListNoError() {
  PreviewBillingScreen(
      connected = BillingState.DISCONNECTED,
      skuList = PREVIEW_SKUS,
      error = null,
  )
}

@Preview
@Composable
private fun PreviewBillingScreenDisconnectedWithNoListNoError() {
  PreviewBillingScreen(
      connected = BillingState.DISCONNECTED,
      skuList = remember { mutableStateListOf() },
      error = null,
  )
}

@Preview
@Composable
private fun PreviewBillingScreenDisconnectedWithNoListError() {
  PreviewBillingScreen(
      connected = BillingState.DISCONNECTED,
      skuList = remember { mutableStateListOf() },
      error = RuntimeException("TEST"),
  )
}

@Preview
@Composable
private fun PreviewBillingScreenLoadingWithListError() {
  PreviewBillingScreen(
      connected = BillingState.LOADING,
      skuList = PREVIEW_SKUS,
      error = RuntimeException("TEST"),
  )
}

@Preview
@Composable
private fun PreviewBillingScreenLoadingWithListNoError() {
  PreviewBillingScreen(
      connected = BillingState.LOADING,
      skuList = PREVIEW_SKUS,
      error = null,
  )
}

@Preview
@Composable
private fun PreviewBillingScreenLoadingWithNoListNoError() {
  PreviewBillingScreen(
      connected = BillingState.LOADING,
      skuList = remember { mutableStateListOf() },
      error = null,
  )
}

@Preview
@Composable
private fun PreviewBillingScreenLoadingWithNoListError() {
  PreviewBillingScreen(
      connected = BillingState.LOADING,
      skuList = remember { mutableStateListOf() },
      error = RuntimeException("TEST"),
  )
}
