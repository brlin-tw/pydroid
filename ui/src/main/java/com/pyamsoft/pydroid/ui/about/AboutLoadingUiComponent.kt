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

package com.pyamsoft.pydroid.ui.about

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.core.bus.Listener
import com.pyamsoft.pydroid.ui.about.AboutStateEvents.LoadComplete
import com.pyamsoft.pydroid.ui.about.AboutStateEvents.Loading
import com.pyamsoft.pydroid.ui.arch.UiComponent
import com.pyamsoft.pydroid.ui.arch.destroy
import com.pyamsoft.pydroid.ui.widget.spinner.SpinnerView
import io.reactivex.Observable

class AboutLoadingUiComponent(
  private val spinnerView: SpinnerView,
  private val owner: LifecycleOwner,
  private val controllerBus: Listener<AboutStateEvents>
) : UiComponent<Unit> {

  override fun id(): Int {
    return spinnerView.id()
  }

  override fun create(savedInstanceState: Bundle?) {
    spinnerView.inflate(savedInstanceState)
    listenForControllerEvents()
  }

  private fun listenForControllerEvents() {
    controllerBus.listen()
        .subscribe {
          when (it) {
            is Loading -> spinnerView.hide()
            is LoadComplete -> spinnerView.show()
          }
        }
        .destroy(owner)
  }

  override fun saveState(outState: Bundle) {
    spinnerView.saveState(outState)
  }

  override fun onUiEvent(): Observable<Unit> {
    return Observable.empty()
  }

}