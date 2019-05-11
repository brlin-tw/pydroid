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

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.CheckResult
import androidx.annotation.StringRes
import com.pyamsoft.pydroid.arch.UiViewImpl
import com.pyamsoft.pydroid.arch.onChange
import com.pyamsoft.pydroid.ui.R

internal class VersionUpgradeContentView internal constructor(
  parent: ViewGroup
) : UiViewImpl<VersionUpgradeViewState, VersionUpgradeViewEvent>(parent) {

  private val upgradeMessage by boundView<TextView>(R.id.upgrade_message)
  private val currentValue by boundView<TextView>(R.id.upgrade_current_value)
  private val newValue by boundView<TextView>(R.id.upgrade_new_value)

  override val layout: Int = R.layout.version_upgrade_content

  override val layoutRoot by boundView<View>(R.id.version_content_root)

  override fun onRender(
    state: VersionUpgradeViewState,
    oldState: VersionUpgradeViewState?
  ) {
    state.onChange(oldState, field = { it.applicationName }) { name ->
      upgradeMessage.text = getString(R.string.upgrade_available_message, name)
    }

    state.onChange(oldState, field = { it.currentVersion }) { version ->
      currentValue.text = "$version"
    }

    state.onChange(oldState, field = { it.newVersion }) { version ->
      newValue.text = "$version"
    }
  }

  override fun onTeardown() {
    upgradeMessage.text = ""
    currentValue.text = ""
    newValue.text = ""
  }

  @CheckResult
  private fun getString(@StringRes id: Int, vararg formatArgs: Any): String {
    return layoutRoot.context.getString(id, *formatArgs)
  }

}
