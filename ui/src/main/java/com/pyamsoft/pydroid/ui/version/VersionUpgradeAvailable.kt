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

package com.pyamsoft.pydroid.ui.version

import androidx.annotation.CheckResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.pyamsoft.pydroid.core.Logger
import com.pyamsoft.pydroid.core.requireNotNull
import com.pyamsoft.pydroid.inject.Injector
import com.pyamsoft.pydroid.ui.internal.app.AppComponent
import com.pyamsoft.pydroid.ui.internal.version.VersionCheckScreen
import com.pyamsoft.pydroid.ui.internal.version.VersionCheckViewModeler
import com.pyamsoft.pydroid.ui.internal.version.upgrade.VersionUpgradeDialog

/** Upon upgrade action started, this callback will run */
public typealias OnUpgradeStartedCallback = () -> Unit

/**
 * A self contained class which is able to check for updates and prompt the user to install them
 * in-app. Adopts the theme from whichever composable it is rendered into
 */
public class VersionUpgradeAvailable
internal constructor(
    activity: FragmentActivity,
    private val appName: String,
) {
  // Keep for Dialog showing
  private var activity: FragmentActivity? = null

  internal var viewModel: VersionCheckViewModeler? = null

  init {
    inject(activity)
  }

  private fun inject(activity: FragmentActivity) {
    Injector.obtainFromActivity<AppComponent>(activity).plusVersionCheck().create().inject(this)
  }

  private fun handleUpgrade() {
    val act = activity.requireNotNull { "Lost Activity somewhere! Was destroy() already called?" }
    VersionUpgradeDialog.show(act)
  }

  public fun destroy() {
    Logger.d("Destroy, clear Activity ref")
    activity = null
    viewModel = null
  }

  /**
   * Render into a composable the version check screen upsell
   *
   * Using custom UI
   */
  @Composable
  public fun Render(
      content: @Composable (VersionCheckViewState, OnUpgradeStartedCallback) -> Unit,
  ) {
    val state = viewModel.requireNotNull().state()
    content(state) { handleUpgrade() }
  }

  /** Render into a composable the default version check screen upsell */
  @Composable
  public fun RenderVersionCheckWidget(
      modifier: Modifier = Modifier,
  ) {
    Render { state, onUpgradeStarted ->
      VersionCheckScreen(
          modifier = modifier,
          state = state,
          appName = appName,
          onUpgrade = onUpgradeStarted,
      )
    }
  }

  public companion object {

    /** Create a new version upgrade available UI component */
    @JvmStatic
    @CheckResult
    public fun create(
        activity: FragmentActivity,
        appName: String,
    ): VersionUpgradeAvailable {
      return VersionUpgradeAvailable(
          activity,
          appName,
      )
    }
  }
}
