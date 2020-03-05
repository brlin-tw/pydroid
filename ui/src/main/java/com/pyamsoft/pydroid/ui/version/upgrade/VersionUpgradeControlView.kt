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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.arch.BindingUiView
import com.pyamsoft.pydroid.ui.databinding.VersionUpgradeControlsBinding
import com.pyamsoft.pydroid.ui.util.Snackbreak
import com.pyamsoft.pydroid.ui.util.setOnDebouncedClickListener
import com.pyamsoft.pydroid.ui.version.upgrade.VersionUpgradeViewEvent.Cancel
import com.pyamsoft.pydroid.ui.version.upgrade.VersionUpgradeViewEvent.Upgrade

internal class VersionUpgradeControlView internal constructor(
    private val owner: LifecycleOwner,
    parent: ViewGroup
) : BindingUiView<VersionUpgradeViewState, VersionUpgradeViewEvent, VersionUpgradeControlsBinding>(
    parent
) {

    init {
        doOnInflate {
            binding.upgradeButton.setOnDebouncedClickListener { publish(Upgrade) }
            binding.laterButton.setOnDebouncedClickListener { publish(Cancel) }
        }

        doOnTeardown {
            binding.upgradeButton.setOnClickListener(null)
            binding.laterButton.setOnClickListener(null)
        }
    }

    override fun provideBindingInflater(): (LayoutInflater, ViewGroup) -> VersionUpgradeControlsBinding {
        return VersionUpgradeControlsBinding::inflate
    }

    override fun provideBindingRoot(binding: VersionUpgradeControlsBinding): View {
        return binding.versionControlRoot
    }

    override fun onRender(state: VersionUpgradeViewState) {
        state.throwable.let { throwable ->
            if (throwable == null) {
                clearError()
            } else {
                showError(throwable)
            }
        }
    }

    private fun showError(error: Throwable) {
        Snackbreak.bindTo(owner) {
            make(layoutRoot, error.message ?: "An unexpected error occurred.")
        }
    }

    private fun clearError() {
        Snackbreak.bindTo(owner) {
            dismiss()
        }
    }
}
