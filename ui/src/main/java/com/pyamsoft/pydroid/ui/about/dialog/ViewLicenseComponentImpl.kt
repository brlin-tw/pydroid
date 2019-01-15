/*
 * Copyright 2019 Peter Kenji Yamanaka
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
 *
 */

package com.pyamsoft.pydroid.ui.about.dialog

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.bootstrap.SchedulerProvider
import com.pyamsoft.pydroid.core.bus.EventBus
import com.pyamsoft.pydroid.loader.ImageLoader
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowUiComponent
import com.pyamsoft.pydroid.ui.widget.shadow.DropshadowView
import com.pyamsoft.pydroid.ui.widget.spinner.SpinnerUiComponent
import com.pyamsoft.pydroid.ui.widget.spinner.SpinnerView

internal class ViewLicenseComponentImpl internal constructor(
  private val parent: ViewGroup,
  private val owner: LifecycleOwner,
  private val imageLoader: ImageLoader,
  private val link: String,
  private val name: String,
  private val uiBus: EventBus<LicenseViewEvent>,
  private val controllerBus: EventBus<LicenseStateEvent>,
  private val schedulerProvider: SchedulerProvider
) : ViewLicenseComponent {

  override fun inject(dialog: ViewLicenseDialog) {
    val toolbarView = LicenseToolbarView(parent, name, link, imageLoader, owner, uiBus)
    val dropshadowView = DropshadowView(parent)
    val webviewView = LicenseWebviewView(parent, link, controllerBus)
    val spinnerView = SpinnerView(parent)
    dialog.worker = ViewLicenseWorker(controllerBus, schedulerProvider)
    dialog.toolbarComponent = LicenseToolbarUiComponent(toolbarView, uiBus, schedulerProvider)
    dialog.dropshadowComponent = DropshadowUiComponent(dropshadowView)
    dialog.loadingComponent = SpinnerUiComponent.create(spinnerView, owner, controllerBus)
    dialog.webviewComponent = LicenseWebviewUiComponent(webviewView, owner, controllerBus)
  }

}