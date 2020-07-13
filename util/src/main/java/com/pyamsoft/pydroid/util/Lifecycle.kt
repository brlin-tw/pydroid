/*
 * Copyright 2020 Peter Kenji Yamanaka
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

package com.pyamsoft.pydroid.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent

inline fun LifecycleOwner.doOnDestroy(crossinline func: () -> Unit) {
    this.lifecycle.doOnDestroy(func)
}

inline fun Lifecycle.doOnDestroy(crossinline func: () -> Unit) {
    val self = this
    self.addObserver(object : LifecycleObserver {

        @Suppress("unused")
        @OnLifecycleEvent(ON_DESTROY)
        fun onEvent() {
            self.removeObserver(this)
            func()
        }
    })
}

inline fun LifecycleOwner.doOnCreate(crossinline func: () -> Unit) {
    this.lifecycle.doOnCreate(func)
}

inline fun Lifecycle.doOnCreate(crossinline func: () -> Unit) {
    val self = this

    self.addObserver(object : LifecycleObserver {

        @Suppress("unused")
        @OnLifecycleEvent(ON_CREATE)
        fun onEvent() {
            self.removeObserver(this)
            func()
        }
    })
}

@JvmOverloads
inline fun LifecycleOwner.doOnStart(repeat: Boolean = false, crossinline func: () -> Unit) {
    this.lifecycle.doOnStart(repeat, func)
}

@JvmOverloads
inline fun Lifecycle.doOnStart(repeat: Boolean = false, crossinline func: () -> Unit) {
    val self = this
    val observer = object : LifecycleObserver {

        @Suppress("unused")
        @OnLifecycleEvent(ON_START)
        fun onEvent() {
            if (!repeat) {
                self.removeObserver(this)
            }
            func()
        }
    }

    self.addObserver(observer)
    if (repeat) {
        self.doOnDestroy {
            self.removeObserver(observer)
        }
    }
}

@JvmOverloads
inline fun LifecycleOwner.doOnStop(repeat: Boolean = false, crossinline func: () -> Unit) {
    this.lifecycle.doOnStop(repeat, func)
}

@JvmOverloads
inline fun Lifecycle.doOnStop(repeat: Boolean = false, crossinline func: () -> Unit) {
    val self = this
    val observer = object : LifecycleObserver {

        @Suppress("unused")
        @OnLifecycleEvent(ON_STOP)
        fun onEvent() {
            if (!repeat) {
                self.removeObserver(this)
            }
            func()
        }
    }

    self.addObserver(observer)

    if (repeat) {
        self.doOnDestroy {
            self.removeObserver(observer)
        }
    }
}

@JvmOverloads
inline fun LifecycleOwner.doOnResume(repeat: Boolean = false, crossinline func: () -> Unit) {
    this.lifecycle.doOnResume(repeat, func)
}

@JvmOverloads
inline fun Lifecycle.doOnResume(repeat: Boolean = false, crossinline func: () -> Unit) {
    val self = this
    val observer = object : LifecycleObserver {

        @Suppress("unused")
        @OnLifecycleEvent(ON_RESUME)
        fun onEvent() {
            if (!repeat) {
                self.removeObserver(this)
            }
            func()
        }
    }

    self.addObserver(observer)

    if (repeat) {
        self.doOnDestroy {
            self.removeObserver(observer)
        }
    }
}

@JvmOverloads
inline fun LifecycleOwner.doOnPause(repeat: Boolean = false, crossinline func: () -> Unit) {
    this.lifecycle.doOnPause(repeat, func)
}

@JvmOverloads
inline fun Lifecycle.doOnPause(repeat: Boolean = false, crossinline func: () -> Unit) {
    val self = this
    val observer = object : LifecycleObserver {

        @Suppress("unused")
        @OnLifecycleEvent(ON_PAUSE)
        fun onEvent() {
            if (!repeat) {
                self.removeObserver(this)
            }
            func()
        }
    }

    self.addObserver(observer)
    if (repeat) {
        self.doOnDestroy {
            self.removeObserver(observer)
        }
    }
}
