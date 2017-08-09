/*
 * Copyright 2017 Peter Kenji Yamanaka
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

package com.pyamsoft.pydroid.ui.rating

import com.pyamsoft.pydroid.util.presenter.SchedulerViewPresenter
import io.reactivex.Scheduler
import timber.log.Timber

internal class RatingSavePresenter(private val currentVersion: Int,
    private val interactor: RatingInteractor,
    observeScheduler: Scheduler, subscribeScheduler: Scheduler) : SchedulerViewPresenter<Unit>(
    observeScheduler, subscribeScheduler) {


  fun saveRating(onRatingSaved: () -> Unit, onRatingDialogSaveError: (Throwable) -> Unit) {
    disposeOnStop {
      interactor.saveRating(currentVersion).subscribeOn(backgroundScheduler).observeOn(
          foregroundScheduler).subscribe({
        Timber.d("Saved current version code: %d", currentVersion)
        onRatingSaved()
      }, {
        Timber.e(it, "Error saving rating dialog")
        onRatingDialogSaveError(it)
      })
    }
  }
}
