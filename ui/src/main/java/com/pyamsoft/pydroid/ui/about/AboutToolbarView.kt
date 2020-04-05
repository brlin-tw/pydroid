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

import com.pyamsoft.pydroid.ui.about.AboutViewEvent.UpNavigate
import com.pyamsoft.pydroid.ui.app.ToolbarActivity
import com.pyamsoft.pydroid.ui.widget.internal.ToolbarView

internal class AboutToolbarView internal constructor(
    backstackCount: Int,
    toolbarActivity: ToolbarActivity
) : ToolbarView<AboutViewState, AboutViewEvent>(backstackCount, toolbarActivity) {

    override fun onNavigationClicked() {
        publish(UpNavigate)
    }

    override fun render(state: AboutViewState) {
        toolbarActivity.withToolbar { toolbar ->
            toolbar.title = state.toolbarTitle
        }
    }
}
