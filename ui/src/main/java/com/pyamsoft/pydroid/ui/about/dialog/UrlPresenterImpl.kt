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

import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.bootstrap.SchedulerProvider
import com.pyamsoft.pydroid.core.bus.EventBus
import com.pyamsoft.pydroid.ui.about.dialog.UrlWebviewState.ExternalNavigation
import com.pyamsoft.pydroid.ui.about.dialog.UrlWebviewState.Loading
import com.pyamsoft.pydroid.ui.about.dialog.UrlWebviewState.PageLoaded
import com.pyamsoft.pydroid.ui.arch.BasePresenter
import com.pyamsoft.pydroid.ui.arch.destroy

internal class UrlPresenterImpl internal constructor(
  private val schedulerProvider: SchedulerProvider,
  owner: LifecycleOwner,
  bus: EventBus<UrlWebviewState>
) : BasePresenter<UrlWebviewState, UrlPresenter.Callback>(owner, bus),
    UrlToolbarView.Callback,
    UrlPresenter {

  override fun onBind() {
    listen().subscribeOn(schedulerProvider.backgroundScheduler)
        .observeOn(schedulerProvider.foregroundScheduler)
        .subscribe {
          return@subscribe when (it) {
            is Loading -> callback.onWebviewBegin()
            is PageLoaded -> onPageLoaded(it.url, it.targetPage)
            is ExternalNavigation -> callback.onWebviewExternalNavigationEvent(it.url)
          }
        }
        .destroy(owner)
  }

  private fun onPageLoaded(
    url: String,
    targetPage: Boolean
  ) {
    if (targetPage) {
      callback.onWebviewTargetPageLoaded(url)
    } else {
      callback.onWebviewOtherPageLoaded(url)
    }
  }

  override fun onUnbind() {
  }

  override fun onToolbarMenuItemclicked(
    itemId: Int,
    url: String
  ) {
    callback.onToolbarMenuItemEvent(itemId, url)
  }

  override fun onToolbarNavClicked() {
    callback.onToolbarNavigateEvent()
  }

}
