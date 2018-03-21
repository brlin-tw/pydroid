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

package com.pyamsoft.pydroid.base.version

import com.pyamsoft.pydroid.cache.Cache
import com.pyamsoft.pydroid.cache.SingleRepository
import io.reactivex.Maybe
import io.reactivex.Single

internal class VersionCheckInteractorImpl internal constructor(
  private val network: VersionCheckInteractor,
  private val versionCache: SingleRepository<Int>
) : VersionCheckInteractor, Cache {

  override fun checkVersion(
    bypass: Boolean,
    packageName: String
  ): Single<Int> {
    return Single.defer {
      Maybe.concat(
          versionCache.get(bypass),
          network.checkVersion(bypass, packageName).doOnSuccess { versionCache.set(it) }.toMaybe()
      )
          .firstOrError()
    }
  }

  override fun clearCache() {
    versionCache.clearCache()
  }
}
