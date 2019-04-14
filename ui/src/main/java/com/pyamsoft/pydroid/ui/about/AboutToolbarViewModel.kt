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

package com.pyamsoft.pydroid.ui.about

import com.pyamsoft.pydroid.arch.UiState
import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.ui.about.AboutToolbarViewModel.AboutState
import javax.inject.Inject

internal class AboutToolbarViewModel @Inject internal constructor(
  private val handler: AboutToolbarHandler
) : UiViewModel<AboutState>(
    initialState = AboutState(navigate = false)
), AboutToolbarView.Callback {

  override fun onBind() {
    handler.handle(this)
        .destroy()
  }

  override fun onUnbind() {
  }

  override fun onToolbarNavClicked() {
    setUniqueState(true, old = { it.navigate }) { state, value -> state.copy(navigate = value) }
  }

  data class AboutState(val navigate: Boolean) : UiState
}
