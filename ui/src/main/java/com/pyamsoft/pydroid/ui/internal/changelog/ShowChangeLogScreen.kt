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

package com.pyamsoft.pydroid.ui.internal.changelog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pyamsoft.pydroid.ui.changelog.ChangeLogViewState
import com.pyamsoft.pydroid.ui.internal.widget.DismissableInterruptCard

@Composable
internal fun ShowChangeLogScreen(
    modifier: Modifier = Modifier,
    state: ChangeLogViewState,
    onShowChangeLog: () -> Unit,
    onDismiss: () -> Unit,
) {
  DismissableInterruptCard(
      modifier = modifier,
      show = state.canShow,
      text = "You've updated to the latest version! Thanks!",
      buttonText = "View Changes",
      onButtonClicked = onShowChangeLog,
      onDismiss = onDismiss,
  )
}

@Preview
@Composable
private fun PreviewShowChangeLogScreen() {
  ShowChangeLogScreen(
      state = MutableChangeLogViewState().apply { canShow = true },
      onDismiss = {},
      onShowChangeLog = {},
  )
}
