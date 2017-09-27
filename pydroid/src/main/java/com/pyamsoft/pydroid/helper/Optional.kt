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

package com.pyamsoft.pydroid.helper

import android.support.annotation.CheckResult

sealed class Optional<out T : Any> {

  @CheckResult
  fun get(): T? = when (this) {
    is Present -> value
    is Absent -> null
  }

  internal data class Present<out T : Any>(val value: T) : Optional<T>()
  internal object Absent : Optional<Nothing>()

  companion object {

    @JvmStatic
    @CheckResult
    fun <T : Any> asOptional(source: T?): Optional<T> = source.asOptional()
  }
}

