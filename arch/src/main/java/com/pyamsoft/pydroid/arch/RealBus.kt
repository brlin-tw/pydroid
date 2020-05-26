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
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.withContext
import timber.log.Timber

class RealBus<T : Any> internal constructor(private val context: CoroutineContext) : EventBus<T> {

    @ExperimentalCoroutinesApi
    private val bus by lazy { BroadcastChannel<T>(1) }

    @ExperimentalCoroutinesApi
    override fun publish(event: T) {
        if (!bus.offer(event)) {
            Timber.w("Failed to publish event: $event")
        }
    }

    @ExperimentalCoroutinesApi
    override suspend fun send(event: T) = withContext(context = context) {
        bus.send(event)
    }

    @CheckResult
    @ExperimentalCoroutinesApi
    override suspend fun onEvent(emitter: suspend (event: T) -> Unit) =
        withContext(context = context) {
            bus.openSubscription()
                .consumeEach { emitter(it) }
        }
}
