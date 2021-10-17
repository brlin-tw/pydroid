/*
 * Copyright 2020 Peter Kenji Yamanaka
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
 */

package com.pyamsoft.pydroid.ui.internal.version

import android.content.Context
import androidx.annotation.CheckResult
import androidx.lifecycle.ViewModelProvider
import com.pyamsoft.pydroid.arch.createViewModelFactory
import com.pyamsoft.pydroid.bootstrap.version.VersionModule
import com.pyamsoft.pydroid.bootstrap.version.VersionModule.Parameters
import com.pyamsoft.pydroid.ui.internal.version.upgrade.VersionUpgradeDialog
import com.pyamsoft.pydroid.ui.internal.version.upgrade.VersionUpgradeViewModel
import com.pyamsoft.pydroid.ui.version.VersionCheckActivity

internal interface VersionCheckComponent {

  fun inject(activity: VersionCheckActivity)

  fun inject(dialog: VersionUpgradeDialog)

  interface Factory {

    @CheckResult fun create(): VersionCheckComponent

    data class Parameters
    internal constructor(
        internal val context: Context,
        internal val version: Int,
        internal val isFakeUpgradeChecker: Boolean,
        internal val isFakeUpgradeAvailable: Boolean
    )
  }

  class Impl private constructor(params: Factory.Parameters) : VersionCheckComponent {

    private val checkFactory: ViewModelProvider.Factory
    private val upgradeFactory: ViewModelProvider.Factory

    init {
      // Make this module each time since if it falls out of scope, the in-app update system
      // will crash
      val module =
          VersionModule(
              Parameters(
                  context = params.context.applicationContext,
                  version = params.version,
                  isFakeUpgradeChecker = params.isFakeUpgradeChecker,
                  isFakeUpgradeAvailable = params.isFakeUpgradeAvailable))
      checkFactory = createViewModelFactory { VersionCheckViewModel(module.provideInteractor()) }
      upgradeFactory =
          createViewModelFactory { VersionUpgradeViewModel(module.provideInteractor()) }
    }

    override fun inject(activity: VersionCheckActivity) {
      activity.versionFactory = checkFactory
    }

    override fun inject(dialog: VersionUpgradeDialog) {
      dialog.factory = upgradeFactory
    }

    internal class FactoryImpl internal constructor(private val params: Factory.Parameters) :
        Factory {

      override fun create(): VersionCheckComponent {
        return Impl(params)
      }
    }
  }
}
