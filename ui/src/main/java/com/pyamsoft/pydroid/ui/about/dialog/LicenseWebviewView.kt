/*
 * Copyright 2019 Peter Kenji Yamanaka
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
 *
 */

package com.pyamsoft.pydroid.ui.about.dialog

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.pyamsoft.pydroid.core.bus.Publisher
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.R2
import com.pyamsoft.pydroid.ui.about.dialog.LicenseStateEvent.Complete
import com.pyamsoft.pydroid.ui.about.dialog.LicenseStateEvent.Loaded
import com.pyamsoft.pydroid.ui.about.dialog.LicenseStateEvent.PageError
import com.pyamsoft.pydroid.ui.arch.UiToggleView
import com.pyamsoft.pydroid.ui.arch.UiView
import com.pyamsoft.pydroid.ui.arch.ViewEvent.EMPTY
import com.pyamsoft.pydroid.ui.arch.ViewEvent.EmptyPublisher
import com.pyamsoft.pydroid.util.hyperlink
import timber.log.Timber

internal class LicenseWebviewView internal constructor(
  private val parent: ViewGroup,
  private val link: String,
  private val controllerBus: Publisher<LicenseStateEvent>
) : UiView<EMPTY>(EmptyPublisher), UiToggleView<EMPTY> {

  private lateinit var unbinder: Unbinder
  @field:BindView(R2.id.webview) internal lateinit var webview: WebView

  private var errorToast: Toast? = null

  override fun id(): Int {
    return webview.id
  }

  override fun inflate(savedInstanceState: Bundle?) {
    val root = parent.inflateAndAdd(R.layout.license_webview)
    unbinder = ButterKnife.bind(this, root)

    setupWebviewJavascript()
    setupWebview()
  }

  private fun setupWebview() {
    webview.webViewClient = object : WebViewClient() {

      override fun onPageFinished(
        view: WebView,
        url: String
      ) {
        super.onPageFinished(view, url)
        val fixedUrl = url.trimEnd('/')
        if (fixedUrl == link) {
          Timber.d("Loaded url: $url, show webview")
          controllerBus.publish(Loaded)
        }

        // If we are showing the webview and we've navigated off the url, close the dialog
        if (webview.isVisible && fixedUrl != link) {
          Timber.w("Navigated away from page: $url - close dialog, and open extenally")
          val error = fixedUrl.hyperlink(view.context)
              .navigate()
          controllerBus.publish(PageError(error))
        }

        controllerBus.publish(Complete)
      }

      @RequiresApi(VERSION_CODES.M)
      override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
      ) {
        super.onReceivedError(view, request, error)
        if (request.url.toString() == link) {
          Timber.e("Webview error: ${error.errorCode} ${error.description}")
          controllerBus.publish(Loaded)
        }

        controllerBus.publish(Complete)
      }

      @Suppress("DEPRECATION", "OverridingDeprecatedMember")
      override fun onReceivedError(
        view: WebView,
        errorCode: Int,
        description: String?,
        failingUrl: String?
      ) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        if (failingUrl == link) {
          Timber.e("Webview error: $errorCode $description")
          controllerBus.publish(Loaded)
        }

        controllerBus.publish(Complete)
      }

    }
  }

  @SuppressLint("SetJavaScriptEnabled")
  private fun setupWebviewJavascript() {
    webview.settings.javaScriptEnabled = true
  }

  override fun saveState(outState: Bundle) {
  }

  override fun show() {
    webview.isVisible = true
  }

  override fun hide() {
    webview.isVisible = false
  }

  private fun dismissError() {
    errorToast?.cancel()
    errorToast = null
  }

  override fun teardown() {
    dismissError()
    unbinder.unbind()
  }

  fun pageLoadError(error: ActivityNotFoundException?) {
    if (error != null) {
      dismissError()
      errorToast = Toast.makeText(
          webview.context.applicationContext,
          "No application can handle this URL",
          Toast.LENGTH_SHORT
      )
          .also { it.show() }
    }
  }

  fun loadUrl() {
    webview.loadUrl(link)
  }

}