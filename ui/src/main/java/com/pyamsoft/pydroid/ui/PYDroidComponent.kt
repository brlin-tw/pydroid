/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.pydroid.ui

import android.os.Bundle
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.preference.PreferenceScreen
import com.pyamsoft.pydroid.ui.about.AboutComponent
import com.pyamsoft.pydroid.ui.about.ViewLicenseComponent
import com.pyamsoft.pydroid.ui.app.fragment.AppComponent
import com.pyamsoft.pydroid.ui.app.fragment.SettingsPreferenceComponent
import com.pyamsoft.pydroid.ui.rating.RatingActivity
import com.pyamsoft.pydroid.ui.rating.RatingDialogComponent
import com.pyamsoft.pydroid.ui.version.VersionCheckComponent

internal interface PYDroidComponent {

  fun inject(activity: RatingActivity)

  @CheckResult
  fun plusVersionCheckComponent(): VersionCheckComponent

  @CheckResult
  fun plusAppComponent(
    owner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?
  ): AppComponent

  @CheckResult
  fun plusSettingsComponent(
    owner: LifecycleOwner,
    preferenceScreen: PreferenceScreen,
    hideClearAll: Boolean,
    hideUpgradeInformation: Boolean
  ): SettingsPreferenceComponent

  @CheckResult
  fun plusAboutComponent(
    owner: LifecycleOwner,
    activity: FragmentActivity,
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): AboutComponent

  @CheckResult
  fun plusViewLicenseComponent(
    owner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
    link: String,
    name: String
  ): ViewLicenseComponent

  @CheckResult
  fun plusRatingDialogComponent(
    owner: LifecycleOwner,
    inflater: LayoutInflater,
    container: ViewGroup?,
    changeLogIcon: Int,
    changeLog: SpannedString
  ): RatingDialogComponent
}
