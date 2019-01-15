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
import com.pyamsoft.pydroid.bootstrap.SchedulerProvider
import com.pyamsoft.pydroid.core.bus.EventBus

internal class VersionUpgradeComponentImpl internal constructor(
  private val parent: ViewGroup,
  private val name: String,
  private val currentVersion: Int,
  private val newVersion: Int,
  private val bus: EventBus<VersionViewEvent>,
  private val schedulerProvider: SchedulerProvider
) : VersionUpgradeComponent {

  override fun inject(dialog: VersionUpgradeDialog) {
    val controls = VersionUpgradeControlView(parent, bus)
    val content = VersionUpgradeContentView(parent, name, currentVersion, newVersion)
    dialog.contentComponent = VersionUpgradeContentUiComponent(content)
    dialog.controlsComponent = VersionUpgradeControlsUiComponent(controls, bus, schedulerProvider)
  }
}