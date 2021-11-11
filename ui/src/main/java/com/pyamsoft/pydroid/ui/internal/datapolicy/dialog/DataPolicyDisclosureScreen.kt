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

package com.pyamsoft.pydroid.ui.internal.datapolicy.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.internal.app.AppHeader
import com.pyamsoft.pydroid.ui.internal.test.createNewTestImageLoader

@Composable
internal fun DataPolicyDisclosureScreen(
    modifier: Modifier = Modifier,
    state: DataPolicyDialogViewState,
    imageLoader: ImageLoader,
    onNavigationErrorDismissed: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
) {

  val snackbarHostState = remember { SnackbarHostState() }

  val icon = state.icon
  val name = state.name
  val navigationError = state.navigationError

  Column(
      modifier = modifier,
  ) {
    AppHeader(
        modifier = Modifier.fillMaxWidth(),
        icon = icon,
        name = name,
        imageLoader = imageLoader,
    )

    Surface {
      Column {
        Actions(
            modifier = Modifier.fillMaxWidth(),
            onAccept = onAccept,
            onReject = onReject,
        )

        NavigationError(
            snackbarHostState = snackbarHostState,
            error = navigationError,
            onSnackbarDismissed = onNavigationErrorDismissed,
        )
      }
    }
  }
}

@Composable
private fun Actions(
    modifier: Modifier = Modifier,
    onAccept: () -> Unit,
    onReject: () -> Unit,
) {
  Column(
      modifier = modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
  ) {
    Button(
        onClick = onAccept,
    ) {
      Text(
          text = stringResource(R.string.dpd_accept),
      )
    }
    TextButton(
        modifier = Modifier.padding(top = 8.dp),
        onClick = onReject,
    ) {
      Text(
          text = stringResource(R.string.dpd_reject),
          fontSize = 12.sp,
      )
    }
  }
}

@Composable
private fun NavigationError(
    snackbarHostState: SnackbarHostState,
    error: Throwable?,
    onSnackbarDismissed: () -> Unit,
) {
  SnackbarHost(hostState = snackbarHostState)

  if (error != null) {
    LaunchedEffect(error) {
      snackbarHostState.showSnackbar(
          message = error.message ?: "An unexpected error occurred",
          duration = SnackbarDuration.Long,
      )
      onSnackbarDismissed()
    }
  }
}

@Preview
@Composable
private fun PreviewDataPolicyDisclosureScreen() {
  val context = LocalContext.current

  DataPolicyDisclosureScreen(
      state = DataPolicyDialogViewState(icon = 0, name = "TEST", navigationError = null),
      imageLoader = createNewTestImageLoader(context),
      onNavigationErrorDismissed = {},
      onAccept = {},
      onReject = {},
  )
}
