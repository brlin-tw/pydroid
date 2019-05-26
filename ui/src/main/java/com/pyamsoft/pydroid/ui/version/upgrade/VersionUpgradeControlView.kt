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

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.arch.BaseUiView
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.util.Snackbreak
import com.pyamsoft.pydroid.ui.util.setOnDebouncedClickListener
import com.pyamsoft.pydroid.ui.version.upgrade.VersionUpgradeViewEvent.Cancel
import com.pyamsoft.pydroid.ui.version.upgrade.VersionUpgradeViewEvent.Upgrade

internal class VersionUpgradeControlView internal constructor(
  private val owner: LifecycleOwner,
  parent: ViewGroup
) : BaseUiView<VersionUpgradeViewState, VersionUpgradeViewEvent>(parent) {

  private val upgradeButton by boundView<Button>(R.id.upgrade_button)
  private val laterButton by boundView<Button>(R.id.later_button)

  override val layout: Int = R.layout.version_upgrade_controls

  override val layoutRoot by boundView<View>(R.id.version_control_root)

  override fun onInflated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    upgradeButton.setOnDebouncedClickListener { publish(Upgrade) }
    laterButton.setOnDebouncedClickListener { publish(Cancel) }
  }

  override fun onRender(
    state: VersionUpgradeViewState,
    savedInstanceState: Bundle?
  ) {
    state.throwable.let { throwable ->
      if (throwable == null) {
        clearError()
      } else {
        showError(throwable)
      }
    }
  }

  private fun showError(error: Throwable) {
    Snackbreak.bindTo(owner)
        .short(layoutRoot, error.message ?: "An unexpected error occurred.")
        .show()
  }

  private fun clearError() {
    Snackbreak.bindTo(owner)
        .dismiss()
  }

  override fun onTeardown() {
    upgradeButton.setOnClickListener(null)
    laterButton.setOnClickListener(null)
    clearError()
  }
}
