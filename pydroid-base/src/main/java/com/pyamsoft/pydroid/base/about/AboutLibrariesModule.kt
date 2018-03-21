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

package com.pyamsoft.pydroid.base.about

import android.support.annotation.CheckResult
import com.pyamsoft.pydroid.PYDroidModule
import com.pyamsoft.pydroid.cache.cacheMany
import io.reactivex.Scheduler

class AboutLibrariesModule(pyDroidModule: PYDroidModule) {

  private val cacheInteractor: AboutLibrariesInteractor
  private val computationScheduler: Scheduler = pyDroidModule.provideComputationScheduler()
  private val ioScheduler: Scheduler = pyDroidModule.provideIoScheduler()
  private val mainThreadScheduler: Scheduler = pyDroidModule.provideMainThreadScheduler()

  init {
    val dataSource = AboutLibrariesDataSourceImpl(pyDroidModule.provideContext())

    val disk = AboutLibrariesInteractorDisk(dataSource)
    val licenseCache = cacheMany<AboutLibrariesModel>()
    cacheInteractor = AboutLibrariesInteractorImpl(disk, licenseCache)
  }

  @CheckResult
  fun getPresenter(): AboutLibrariesPresenter {
    return AboutLibrariesPresenter(
        cacheInteractor, computationScheduler, ioScheduler, mainThreadScheduler
    )
  }
}
