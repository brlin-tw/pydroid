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

package com.pyamsoft.pydroid.ui.internal.billing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.internal.widget.DismissableInterruptCard

@Composable
internal fun ShowBillingUpsell(
    modifier: Modifier = Modifier,
    state: BillingViewState,
    onShowBilling: () -> Unit,
    onDismiss: () -> Unit,
) {
  DismissableInterruptCard(
      modifier = modifier,
      show = state.showUpsell,
      text = stringResource(R.string.donate_summary),
      buttonText = stringResource(R.string.donate_title),
      onDismiss = onDismiss,
      onButtonClicked = onShowBilling,
  )
}

@Preview
@Composable
private fun PreviewShowBillingUpsell() {
  ShowBillingUpsell(
      state = MutableBillingViewState().apply { showUpsell = true },
      onDismiss = {},
      onShowBilling = {},
  )
}
