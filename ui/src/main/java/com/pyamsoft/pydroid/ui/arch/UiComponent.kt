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

package com.pyamsoft.pydroid.ui.arch

import android.os.Bundle
import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.Observable

interface UiComponent<T : ViewEvent> {

  @CheckResult
  @IdRes
  fun id(): Int

  fun create(savedInstanceState: Bundle?)

  fun saveState(outState: Bundle)

  @CheckResult
  fun onUiEvent(): Observable<T>

  fun LifecycleOwner.runOnDestroy(func: () -> Unit) {
    lifecycle.addObserver(object : LifecycleObserver {

      @Suppress("unused")
      @OnLifecycleEvent(ON_DESTROY)
      fun onDestroy() {
        lifecycle.removeObserver(this)
        func()
      }
    })
  }

}