/*
 * Copyright 2022 Peter Kenji Yamanaka
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

package com.pyamsoft.pydroid.ui.internal.rating

import com.pyamsoft.highlander.highlander
import com.pyamsoft.pydroid.arch.AbstractViewModeler
import com.pyamsoft.pydroid.arch.UnitViewState
import com.pyamsoft.pydroid.bootstrap.rating.AppRatingLauncher
import com.pyamsoft.pydroid.bootstrap.rating.RatingInteractor
import com.pyamsoft.pydroid.core.Logger
import com.pyamsoft.pydroid.core.ResultWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class RatingViewModeler
internal constructor(
    interactor: RatingInteractor,
) : AbstractViewModeler<UnitViewState>(UnitViewState) {

  private val loadRunner =
      highlander<ResultWrapper<AppRatingLauncher>> { interactor.askForRating() }

  internal fun loadInAppRating(
      scope: CoroutineScope,
      onLaunchInAppRating: (AppRatingLauncher) -> Unit
  ) {
    scope.launch(context = Dispatchers.Main) {
      loadRunner
          .call()
          .onSuccess { Logger.d("Launch in-app rating: $it") }
          .onSuccess { onLaunchInAppRating(it) }
          .onFailure { Logger.e(it, "Unable to launch in-app rating") }
    }
  }
}
