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

package com.pyamsoft.pydroid.presenter

import com.pyamsoft.pydroid.data.enforceComputation
import com.pyamsoft.pydroid.data.enforceIo
import com.pyamsoft.pydroid.data.enforceMainThread
import io.reactivex.Scheduler

abstract class SchedulerPresenter<V : Any> protected constructor(
  protected val computationScheduler: Scheduler,
  protected val ioScheduler: Scheduler,
  protected val mainThreadScheduler: Scheduler,
  enforce: Boolean = true
) : Presenter<V>() {

  constructor(
    computationScheduler: Scheduler,
    ioScheduler: Scheduler,
    mainThreadScheduler: Scheduler
  ) : this(computationScheduler, ioScheduler, mainThreadScheduler, enforce = false)

  init {
    if (enforce) {
      computationScheduler.enforceComputation()
      ioScheduler.enforceIo()
      mainThreadScheduler.enforceMainThread()
    }
  }
}
