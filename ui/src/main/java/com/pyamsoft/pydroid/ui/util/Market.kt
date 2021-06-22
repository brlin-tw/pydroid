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
 */

package com.pyamsoft.pydroid.ui.util

import android.content.Context
import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.core.ResultWrapper
import com.pyamsoft.pydroid.ui.internal.util.MarketLinker

@CheckResult
internal fun MarketLinker.openAppPage(context: Context): ResultWrapper<Unit> {
  val app = context.applicationContext
  val link = app.packageName
  return linkToMarketPage(app, link)
}

@CheckResult
internal fun MarketLinker.openDevPage(context: Context): ResultWrapper<Unit> {
  val app = context.applicationContext
  return linkToDeveloperPage(app)
}
