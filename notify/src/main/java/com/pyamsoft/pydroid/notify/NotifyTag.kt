/*
 * Copyright 2023 pyamsoft
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

package com.pyamsoft.pydroid.notify

import androidx.annotation.CheckResult

/** Represents a Notification Tag for a NotifyDispatcher */
public data class NotifyTag
internal constructor(
    /** Notification Tag */
    val tag: String
)

/** Converts an Android system Notification tag to a NotifyTag */
@CheckResult
public fun String.toNotifyTag(): NotifyTag {
  return NotifyTag(this)
}
