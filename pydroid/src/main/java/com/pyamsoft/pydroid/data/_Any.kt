/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.pydroid.data

import android.support.annotation.CheckResult
import timber.log.Timber

@CheckResult
inline fun <reified T : Any> T?.elseDefault(crossinline fallback: () -> T): T {
  if (this == null) {
    Timber.w("Expected not-null ${T::class.java.simpleName}")
    return fallback()
  } else {
    return this
  }
}