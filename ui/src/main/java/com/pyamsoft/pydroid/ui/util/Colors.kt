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

package com.pyamsoft.pydroid.ui.util

import androidx.annotation.CheckResult
import androidx.compose.material.ElevationOverlay
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Copied out of [Surface] and exposed to public callers
 *
 * Defaults color to Surface color
 */
@Composable
@CheckResult
public fun surfaceColorAtElevation(
    color: Color = MaterialTheme.colors.surface,
    elevationOverlay: ElevationOverlay? = LocalElevationOverlay.current,
    elevation: Dp,
): Color {
  return if (color == MaterialTheme.colors.surface && elevationOverlay != null) {
    elevationOverlay.apply(color, elevation)
  } else {
    color
  }
}
