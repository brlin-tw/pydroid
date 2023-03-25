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

package com.pyamsoft.pydroid.ui.internal.app

import android.app.Activity
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * A Compose theme which does nothing
 *
 * Can't use object literal or we lose @Composable context
 */
@Suppress("ObjectLiteralToLambda")
internal val NoopTheme: ComposeTheme =
    object : ComposeTheme {

      @Composable
      override fun Render(
          activity: Activity,
          content: @Composable () -> Unit,
      ) {
        // We update the LocalContentColor to match our onBackground. This allows the default
        // content color to be more appropriate to the theme background
        //
        // This is what a default MaterialTheme does. Otherwise, do nothing else.
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colors.onBackground,
            content = content,
        )
      }
    }
