/*
 * Copyright 2024 pyamsoft
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.pydroid.ui.internal.widget

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pyamsoft.pydroid.theme.keylines

/**
 * In Light Mode, surfaces at the same elevation have a very thin line between them
 *
 * We can work around that with this Composable BUT, it will not have the surface's Shadows
 */
@Composable
internal fun BetterSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(color),
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
  CompositionLocalProvider(
      LocalContentColor provides contentColor,
  ) {
    Box(
        modifier =
            modifier
                .background(
                    color =
                        MaterialTheme.colorScheme.surfaceColorAtElevation(
                            elevation = elevation,
                        ),
                    shape = shape,
                )
                .run {
                  if (border != null) {
                    border(
                        border = border,
                        shape = shape,
                    )
                  } else {
                    this
                  }
                },
    ) {
      content()
    }
  }
}

@Preview
@Composable
private fun PreviewBetterSurface() {
  Surface(
      modifier = Modifier.background(color = Color.Red).padding(MaterialTheme.keylines.content),
  ) {
    Column {
      BetterSurface(
          modifier = Modifier.padding(MaterialTheme.keylines.content),
      ) {
        Text(
            text = "Hello, World!",
        )
      }
      BetterSurface(
          modifier = Modifier.padding(MaterialTheme.keylines.content),
      ) {
        Text(
            text = "Better than you!",
        )
      }
    }
  }
}
