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

package com.pyamsoft.pydroid.arch

import androidx.annotation.CheckResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import timber.log.Timber

@ExperimentalCoroutinesApi
class EventBus<T : Any> private constructor() {

  private val bus = BroadcastChannel<T>(1)

  fun publish(event: T) {
    if (!bus.offer(event)) {
      Timber.w("Failed to offer event onto bus: $event")
    }
  }

  internal suspend inline fun onEvent(crossinline func: (event: T) -> Unit) {
    bus.openSubscription()
        .consumeEach(func)
  }

  companion object {

    private val EMPTY by lazy { create<Unit>() }

    @JvmStatic
    @CheckResult
    fun <T : Any> create(): EventBus<T> = EventBus()

    @JvmStatic
    @CheckResult
    fun empty(): EventBus<Unit> = EMPTY
  }
}
