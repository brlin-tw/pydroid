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

package com.pyamsoft.pydroid.ui

import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.arch.UiViewModelFactory
import com.pyamsoft.pydroid.bootstrap.about.AboutInteractor
import com.pyamsoft.pydroid.bootstrap.rating.RatingInteractor
import com.pyamsoft.pydroid.bootstrap.version.VersionCheckInteractor
import com.pyamsoft.pydroid.ui.about.AboutViewModel
import com.pyamsoft.pydroid.ui.privacy.PrivacyViewModel
import com.pyamsoft.pydroid.ui.rating.RatingViewModel
import com.pyamsoft.pydroid.ui.rating.dialog.RatingDialogViewModel
import com.pyamsoft.pydroid.ui.settings.AppSettingsViewModel
import com.pyamsoft.pydroid.ui.theme.Theming
import com.pyamsoft.pydroid.ui.version.VersionCheckViewModel
import com.pyamsoft.pydroid.ui.version.upgrade.VersionUpgradeViewModel
import kotlin.reflect.KClass

internal class PYDroidViewModelFactory internal constructor(
    private val name: String,
    private val version: Int,
    private val ratingInteractor: RatingInteractor,
    private val aboutInteractor: AboutInteractor,
    private val versionInteractor: VersionCheckInteractor,
    private val theming: Theming
) : UiViewModelFactory() {

    override fun <T : UiViewModel<*, *, *>> viewModel(modelClass: KClass<T>): UiViewModel<*, *, *> {
        return when (modelClass) {
            RatingDialogViewModel::class -> RatingDialogViewModel(ratingInteractor)
            VersionUpgradeViewModel::class -> VersionUpgradeViewModel(name, version)
            AboutViewModel::class -> AboutViewModel(aboutInteractor)
            RatingViewModel::class -> RatingViewModel(ratingInteractor)
            AppSettingsViewModel::class -> AppSettingsViewModel(theming)
            VersionCheckViewModel::class -> VersionCheckViewModel(versionInteractor)
            PrivacyViewModel::class -> PrivacyViewModel()
            else -> fail()
        }
    }
}
