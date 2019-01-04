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

package com.pyamsoft.pydroid.bootstrap.version

import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.bootstrap.version.api.MinimumApiProvider
import com.pyamsoft.pydroid.bootstrap.version.network.NetworkStatusProvider
import com.pyamsoft.pydroid.bootstrap.version.network.NoNetworkException
import com.pyamsoft.pydroid.core.threads.Enforcer
import io.reactivex.Maybe
import io.reactivex.Single

internal class VersionCheckInteractorNetwork internal constructor(
  private val enforcer: Enforcer,
  private val minimumApiProvider: MinimumApiProvider,
  private val networkStatusProvider: NetworkStatusProvider,
  private val versionCheckService: VersionCheckService
) : VersionCheckInteractor {

  @CheckResult
  private fun versionCodeForApi(
    response: VersionCheckResponse
  ): Int {
    enforcer.assertNotOnMainThread()
    val minApi = minimumApiProvider.minApi()
    var versionCode = 0
    response.responseObjects()
        .asSequence()
        .sortedBy { it.minApi() }
        .forEach {
          if (it.minApi() <= minApi) {
            versionCode = it.version()
          }
        }
    return versionCode
  }

  override fun checkVersion(
    force: Boolean,
    packageName: String
  ): Maybe<Int> {
    return Single.defer {
      enforcer.assertNotOnMainThread()
      if (!networkStatusProvider.hasConnection()) {
        throw NoNetworkException
      } else {
        val targetName: String
        if (packageName.endsWith(".dev")) {
          targetName = packageName.substringBefore(".dev")
        } else {
          targetName = packageName
        }
        return@defer versionCheckService.checkVersion(targetName)
      }
    }
        .map { versionCodeForApi(it) }
        .toMaybe()
  }
}
