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

package com.pyamsoft.pydroid.ui.version.upgrade

import android.view.ViewGroup
import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.ui.version.upgrade.VersionUpgradeComponent.VersionModule
import dagger.Binds
import dagger.BindsInstance
import dagger.Module
import dagger.Subcomponent
import javax.inject.Named

@Subcomponent(modules = [VersionModule::class])
internal interface VersionUpgradeComponent {

  fun inject(dialog: VersionUpgradeDialog)

  @Subcomponent.Factory
  interface Factory {

    @CheckResult
    fun create(
      @BindsInstance parent: ViewGroup,
      @BindsInstance @Named("new_version") newVersion: Int
    ): VersionUpgradeComponent

  }

  @Module
  abstract class VersionModule {

    @Binds
    @CheckResult
    internal abstract fun bindUiComponent(impl: VersionUpgradeUiComponentImpl): VersionUpgradeUiComponent

    @Binds
    @CheckResult
    internal abstract fun bindUiCallback(impl: VersionUpgradeHandler): VersionUpgradeControlView.Callback

  }
}
