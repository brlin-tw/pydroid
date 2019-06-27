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

import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.ui.about.AboutToolbarControllerEvent.Navigation
import com.pyamsoft.pydroid.ui.about.AboutToolbarViewEvent.UpNavigate
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal class AboutToolbarViewModel internal constructor(
) : UiViewModel<AboutToolbarState, AboutToolbarViewEvent, AboutToolbarControllerEvent>(
    initialState = AboutToolbarState(title = "Open Source Licenses")
) {

  override fun handleViewEvent(event: AboutToolbarViewEvent) {
    return when (event) {
      is UpNavigate -> publish(Navigation)
    }
  }
}
