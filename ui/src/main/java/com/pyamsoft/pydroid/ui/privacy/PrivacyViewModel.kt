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

package com.pyamsoft.pydroid.ui.privacy

import android.app.Activity
import android.content.ActivityNotFoundException
import androidx.lifecycle.viewModelScope
import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.ui.privacy.PrivacyControllerEvent.ViewExternalPolicy
import com.pyamsoft.pydroid.ui.privacy.PrivacyViewEvent.SnackbarHidden
import com.pyamsoft.pydroid.util.hyperlink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class PrivacyViewModel internal constructor(
  activity: Activity
) : UiViewModel<PrivacyViewState, PrivacyViewEvent, PrivacyControllerEvent>(
    initialState = PrivacyViewState(throwable = null)
) {

  private var activity: Activity? = activity

  override fun onInit() {
    viewModelScope.launch(context = Dispatchers.Default) {
      PrivacyEventBus.onEvent { event ->
        withContext(context = Dispatchers.Main) {
          publish(ViewExternalPolicy(event.url.hyperlink(requireNotNull(activity))))
        }
      }
    }
  }

  override fun handleViewEvent(event: PrivacyViewEvent) {
    return when (event) {
      is SnackbarHidden -> setState { copy(throwable = null) }
    }
  }

  override fun onTeardown() {
    activity = null
  }

  fun navigationFailed(error: ActivityNotFoundException) {
    setState { copy(throwable = error) }
  }

  fun navigationSuccess() {
    setState { copy(throwable = null) }
  }

}