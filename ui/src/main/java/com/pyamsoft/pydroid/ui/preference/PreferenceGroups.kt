/*
 * Copyright 2021 Peter Kenji Yamanaka
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

package com.pyamsoft.pydroid.ui.preference

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
@JvmOverloads
internal fun PreferenceGroupHeader(
    modifier: Modifier = Modifier,
    name: String,
) {
  Box(
      contentAlignment = Alignment.CenterStart,
      modifier = modifier.padding(16.dp).padding(start = 48.dp),
  ) {
    Text(
        text = name,
        style =
            MaterialTheme.typography.body2.copy(
                color = MaterialTheme.colors.secondary,
                fontWeight = FontWeight.Bold,
            ),
    )
  }
}