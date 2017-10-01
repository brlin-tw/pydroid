/*
 *     Copyright (C) 2017 Peter Kenji Yamanaka
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.pyamsoft.pydroid.about

import android.support.annotation.CheckResult
import com.pyamsoft.pydroid.PYDroidModule
import io.reactivex.Scheduler

class AboutLibrariesModule(pyDroidModule: PYDroidModule) {

  private val cacheInteractor: AboutLibrariesInteractor
  private val computationScheduler: Scheduler = pyDroidModule.provideComputationScheduler()
  private val ioScheduler: Scheduler = pyDroidModule.provideIoScheduler()
  private val mainThreadScheduler: Scheduler = pyDroidModule.provideMainThreadScheduler()

  init {
    val dataSource: AboutLibrariesDataSource = AboutLibrariesDataSourceImpl(
        pyDroidModule.provideContext())
    val interactor: AboutLibrariesInteractor = AboutLibrariesInteractorImpl(dataSource)
    cacheInteractor = AboutLibrariesInteractorCache(interactor)
  }

  @CheckResult
  fun getPresenter(): AboutLibrariesPresenter {
    return AboutLibrariesPresenter(cacheInteractor, computationScheduler, ioScheduler,
        mainThreadScheduler)
  }
}
