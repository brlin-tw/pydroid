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

package com.pyamsoft.pydroid.bootstrap.about

import com.popinnow.android.repo.Repo
import com.pyamsoft.pydroid.core.cache.Cache
import com.pyamsoft.pydroid.core.threads.Enforcer
import io.reactivex.Single

internal class AboutLibrariesInteractorImpl internal constructor(
  private val enforcer: Enforcer,
  private val disk: AboutLibrariesInteractor,
  private val repo: Repo<List<AboutLibrariesModel>>
) : AboutLibrariesInteractor, Cache {

  override fun loadLicenses(bypass: Boolean): Single<List<AboutLibrariesModel>> {
    return repo.get(bypass) {
      enforcer.assertNotOnMainThread()
      return@get disk.loadLicenses(true)
    }
  }

  override fun clearCache() {
    repo.clearAll()
  }
}
