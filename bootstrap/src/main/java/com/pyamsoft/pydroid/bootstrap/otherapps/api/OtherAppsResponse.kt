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

package com.pyamsoft.pydroid.bootstrap.otherapps.api

import androidx.annotation.CheckResult
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Collections

@JsonClass(generateAdapter = true)
internal data class OtherAppsResponse internal constructor(
    @field:Json(name = "apps")
    internal val apps: List<OtherAppsResponseEntry>?
) {

    @CheckResult
    fun apps(): List<OtherAppsResponseEntry> {
        return apps.let {
            if (it == null) emptyList() else Collections.unmodifiableList(it)
        }
    }

    // Needed so we can generate a static adapter
    companion object
}