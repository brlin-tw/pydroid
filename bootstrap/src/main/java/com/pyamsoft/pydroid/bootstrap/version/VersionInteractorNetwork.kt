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

package com.pyamsoft.pydroid.bootstrap.version

import com.pyamsoft.pydroid.core.Enforcer
import com.pyamsoft.pydroid.core.Logger
import com.pyamsoft.pydroid.core.ResultWrapper
import com.pyamsoft.pydroid.util.ifNotCancellation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class VersionInteractorNetwork internal constructor(private val updater: AppUpdater) :
    VersionInteractor {

  override suspend fun watchForDownloadComplete(onDownloadCompleted: () -> Unit) =
      throw IllegalStateException("This should never be called directly")

  override suspend fun completeUpdate() =
      throw IllegalStateException("This should never be called directly")

  override suspend fun checkVersion(force: Boolean): ResultWrapper<AppUpdateLauncher> =
      withContext(context = Dispatchers.IO) {
        Enforcer.assertOffMainThread()

        return@withContext try {
          ResultWrapper.success(updater.checkForUpdate())
        } catch (e: Throwable) {
          e.ifNotCancellation {
            Logger.e(e, "Failed to check for updates")
            ResultWrapper.failure(e)
          }
        }
      }
}
