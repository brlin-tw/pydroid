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

package com.pyamsoft.pydroid.ui.rating

import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.bootstrap.SchedulerProvider
import com.pyamsoft.pydroid.bootstrap.rating.RatingInteractor
import com.pyamsoft.pydroid.core.bus.EventBus
import com.pyamsoft.pydroid.ui.arch.Worker
import com.pyamsoft.pydroid.ui.rating.RatingStateEvent.ShowEvent
import io.reactivex.disposables.Disposable

internal class RatingWorker internal constructor(
  private val interactor: RatingInteractor,
  private val schedulerProvider: SchedulerProvider,
  bus: EventBus<RatingStateEvent>
) : Worker<RatingStateEvent>(bus) {

  @CheckResult
  fun onRatingDialogRequested(func: () -> Unit): Disposable {
    return listen()
        .subscribeOn(schedulerProvider.backgroundScheduler)
        .observeOn(schedulerProvider.foregroundScheduler)
        .ofType(ShowEvent::class.java)
        .subscribe { func() }
  }

  @CheckResult
  fun loadRatingDialog(force: Boolean): Disposable {
    return interactor.needsToViewRating(force)
        .filter { it }
        .map { Unit }
        .subscribeOn(schedulerProvider.backgroundScheduler)
        .observeOn(schedulerProvider.foregroundScheduler)
        .subscribe { publish(ShowEvent) }
  }

}