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

package com.pyamsoft.pydroid.base.version

import androidx.annotation.CheckResult
import com.google.auto.value.AutoValue
import com.pyamsoft.pydroid.data.elseDefault
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.util.Collections

@AutoValue
internal abstract class VersionCheckResponse internal constructor() {

  @CheckResult
  @Json(name = "response_objects")
  protected abstract fun internalResponseObjects(): List<ResponseObject>?

  @CheckResult
  fun responseObjects(): List<ResponseObject> {
    return Collections.unmodifiableList(
        internalResponseObjects().elseDefault { emptyList() }
    )
  }

  @AutoValue
  internal abstract class ResponseObject internal constructor() {

    @CheckResult
    @Json(name = "min_api")
    abstract fun minApi(): Int

    @CheckResult
    abstract fun version(): Int

    companion object {

      @JvmStatic
      @CheckResult
      fun typeAdapter(moshi: Moshi): JsonAdapter<ResponseObject> =
        AutoValue_VersionCheckResponse_ResponseObject.MoshiJsonAdapter(moshi)
    }
  }

  companion object {

    @JvmStatic
    @CheckResult
    fun typeAdapter(moshi: Moshi): JsonAdapter<VersionCheckResponse> =
      AutoValue_VersionCheckResponse.MoshiJsonAdapter(moshi)
  }
}
