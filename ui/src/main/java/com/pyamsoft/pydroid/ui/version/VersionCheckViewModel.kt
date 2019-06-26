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

package com.pyamsoft.pydroid.ui.version

import androidx.lifecycle.viewModelScope
import com.pyamsoft.pydroid.arch.UiViewModel
import com.pyamsoft.pydroid.arch.UnitViewEvent
import com.pyamsoft.pydroid.arch.singleJob
import com.pyamsoft.pydroid.bootstrap.version.VersionCheckInteractor
import com.pyamsoft.pydroid.ui.version.VersionControllerEvent.ShowUpgrade
import com.pyamsoft.pydroid.ui.version.VersionViewState.Loading
import com.pyamsoft.pydroid.ui.version.VersionViewState.UpgradePayload
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
internal class VersionCheckViewModel internal constructor(
  private val interactor: VersionCheckInteractor
) : UiViewModel<VersionViewState, UnitViewEvent, VersionControllerEvent>(
    initialState = VersionViewState(
        isLoading = null,
        throwable = null
    )
) {

  private var checkUpdateJob by singleJob()

  init {
    checkForUpdates(false)
  }

  override fun onTeardown() {
    checkUpdateJob.cancel()
  }

  override fun handleViewEvent(event: UnitViewEvent) {
  }

  private fun handleVersionCheckBegin(forced: Boolean) {
    setState { copy(isLoading = Loading(forced)) }
  }

  private fun handleVersionCheckFound(
    currentVersion: Int,
    newVersion: Int
  ) {
    setState { copy(throwable = null) }
    publish(ShowUpgrade(UpgradePayload(currentVersion, newVersion)))
  }

  private fun handleVersionCheckError(throwable: Throwable) {
    setState { copy(throwable = throwable) }
  }

  private fun handleVersionCheckComplete() {
    setState { copy(isLoading = null) }
  }

  internal fun checkForUpdates(force: Boolean) {
    checkUpdateJob = viewModelScope.launch {
      handleVersionCheckBegin(force)
      try {
        val runner = async(Dispatchers.Default) { interactor.checkVersion(force) }
        val version = runner.await()
        if (version != null) {
          handleVersionCheckFound(version.currentVersion, version.newVersion)
        }
      } catch (e: Throwable) {
        if (e !is CancellationException) {
          Timber.e(e, "Error checking for latest version")
          handleVersionCheckError(e)
        }
      } finally {
        handleVersionCheckComplete()
      }
    }
  }

}
