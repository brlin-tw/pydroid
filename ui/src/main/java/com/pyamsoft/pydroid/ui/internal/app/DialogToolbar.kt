/*
 * Copyright 2022 Peter Kenji Yamanaka
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

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.theme.ZeroElevation

@Composable
internal fun DialogToolbar(
    modifier: Modifier = Modifier,
    title: String,
    onClose: () -> Unit,
) {
  Surface(
      modifier = modifier,
      color = MaterialTheme.colors.primary,
      contentColor = MaterialTheme.colors.onPrimary,
      elevation = ZeroElevation,
      shape =
          MaterialTheme.shapes.medium.copy(
              bottomEnd = ZeroCornerSize,
              bottomStart = ZeroCornerSize,
          ),
  ) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.Transparent,
        contentColor = LocalContentColor.current,
        elevation = ZeroElevation,
        title = {
          Text(
              text = title,
          )
        },
        navigationIcon = {
          IconButton(
              onClick = onClose,
          ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.close),
            )
          }
        },
    )
  }
}

@Preview
@Composable
private fun PreviewDialogToolbar() {
  DialogToolbar(
      title = "TEST",
      onClose = {},
  )
}
