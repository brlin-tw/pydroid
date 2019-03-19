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

import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.arch.BaseUiComponent
import com.pyamsoft.pydroid.arch.doOnDestroy
import com.pyamsoft.pydroid.ui.about.dialog.UrlUiComponent.Callback
import com.pyamsoft.pydroid.ui.navigation.FailedNavigationPresenter
import com.pyamsoft.pydroid.ui.widget.spinner.SpinnerView

internal class UrlUiComponentImpl internal constructor(
  private val webview: UrlWebviewView,
  private val spinner: SpinnerView,
  private val presenter: UrlPresenter,
  private val failedNavigationPresenter: FailedNavigationPresenter
) : BaseUiComponent<UrlUiComponent.Callback>(),
    UrlUiComponent,
    UrlPresenter.Callback {

  override fun id(): Int {
    return webview.id()
  }

  override fun onBind(
    owner: LifecycleOwner,
    savedInstanceState: Bundle?,
    callback: Callback
  ) {
    owner.doOnDestroy {
      webview.teardown()
      spinner.teardown()
      presenter.unbind()
    }

    webview.inflate(savedInstanceState)
    spinner.inflate(savedInstanceState)
    presenter.bind(this)

    // This looks weird because the webview is the state controller and the view...
    webview.loadUrl()
  }

  override fun onLayout(set: ConstraintSet) {
    spinner.also {
      set.connect(it.id(), ConstraintSet.TOP, webview.id(), ConstraintSet.BOTTOM)
      set.connect(it.id(), ConstraintSet.START, webview.id(), ConstraintSet.START)
      set.connect(it.id(), ConstraintSet.END, webview.id(), ConstraintSet.END)
      set.connect(it.id(), ConstraintSet.BOTTOM, webview.id(), ConstraintSet.BOTTOM)
      set.constrainHeight(it.id(), ConstraintSet.MATCH_CONSTRAINT)
      set.constrainWidth(it.id(), ConstraintSet.MATCH_CONSTRAINT)
    }
  }

  override fun onSaveState(outState: Bundle) {
    webview.saveState(outState)
    spinner.saveState(outState)
  }

  override fun onWebviewBegin() {
    webview.hide()
    spinner.show()
  }

  override fun onWebviewOtherPageLoaded(url: String) {
    webview.hide()
    spinner.show()
  }

  override fun onWebviewTargetPageLoaded(url: String) {
    spinner.hide()
    webview.show()
  }

  override fun onWebviewExternalNavigationEvent(url: String) {
    callback.onNavigateToExternalUrl(url)
  }

  override fun navigationFailed(error: ActivityNotFoundException) {
    failedNavigationPresenter.failedNavigation(error)
  }

}