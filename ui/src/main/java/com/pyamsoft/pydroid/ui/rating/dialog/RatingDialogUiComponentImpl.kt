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

package com.pyamsoft.pydroid.ui.rating.dialog

import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.arch.BaseUiComponent
import com.pyamsoft.pydroid.arch.doOnDestroy
import com.pyamsoft.pydroid.ui.arch.InvalidIdException
import com.pyamsoft.pydroid.ui.navigation.NavigationViewModel
import com.pyamsoft.pydroid.ui.rating.dialog.RatingDialogUiComponent.Callback
import com.pyamsoft.pydroid.ui.rating.dialog.RatingDialogViewModel.RatingState

internal class RatingDialogUiComponentImpl internal constructor(
  private val viewModel: RatingDialogViewModel,
  private val iconView: RatingIconView,
  private val changelogView: RatingChangelogView,
  private val controlsView: RatingControlsView,
  private val navigationViewModel: NavigationViewModel
) : BaseUiComponent<Callback>(),
    RatingDialogUiComponent {

  override fun id(): Int {
    throw InvalidIdException
  }

  override fun onBind(
    owner: LifecycleOwner,
    savedInstanceState: Bundle?,
    callback: Callback
  ) {
    owner.doOnDestroy {
      iconView.teardown()
      changelogView.teardown()
      controlsView.teardown()
      viewModel.unbind()
    }

    iconView.inflate(savedInstanceState)
    changelogView.inflate(savedInstanceState)
    controlsView.inflate(savedInstanceState)
    viewModel.bind { state, oldState ->
      renderRateLink(state, oldState)
    }
  }

  private fun renderRateLink(
    state: RatingState,
    oldState: RatingState?
  ) {
    state.renderOnChange(oldState, value = { it.rateLink }) { link ->
      if (link.isBlank()) {
        callback.onCancelRating()
      } else {
        callback.onNavigateToApplicationPage(link)
      }
    }
  }

  override fun onSaveState(outState: Bundle) {
    iconView.saveState(outState)
    changelogView.saveState(outState)
    controlsView.saveState(outState)
  }

  override fun navigationFailed(error: ActivityNotFoundException) {
    navigationViewModel.failedNavigation(error)
  }

}
