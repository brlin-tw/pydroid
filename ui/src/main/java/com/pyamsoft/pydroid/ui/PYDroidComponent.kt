/*
 * Copyright 2020 Peter Kenji Yamanaka
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

package com.pyamsoft.pydroid.ui

import android.app.Application
import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.bootstrap.about.AboutModule
import com.pyamsoft.pydroid.bootstrap.changelog.ChangeLogModule
import com.pyamsoft.pydroid.bootstrap.datapolicy.DataPolicyModule
import com.pyamsoft.pydroid.bootstrap.network.NetworkModule
import com.pyamsoft.pydroid.bootstrap.otherapps.OtherAppsModule
import com.pyamsoft.pydroid.bootstrap.settings.SettingsModule
import com.pyamsoft.pydroid.bus.EventBus
import com.pyamsoft.pydroid.core.Logger
import com.pyamsoft.pydroid.core.PYDroidLogger
import com.pyamsoft.pydroid.protection.Protection
import com.pyamsoft.pydroid.ui.app.ComposeThemeFactory
import com.pyamsoft.pydroid.ui.app.ComposeThemeProvider
import com.pyamsoft.pydroid.ui.internal.app.AppComponent
import com.pyamsoft.pydroid.ui.internal.preference.PYDroidPreferencesImpl
import com.pyamsoft.pydroid.ui.theme.Theming
import com.pyamsoft.pydroid.ui.theme.ThemingImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

internal interface PYDroidComponent {

  @CheckResult fun plusApp(): AppComponent.Factory

  interface Factory {

    @CheckResult fun create(params: Component.Parameters): Component
  }

  interface Component : PYDroidComponent {

    @CheckResult fun moduleProvider(): ModuleProvider

    data class Parameters
    internal constructor(
        override val googlePlayLicenseVerificationKey: String,
        override val imageLoader: () -> coil.ImageLoader,
        override val privacyPolicyUrl: String,
        override val bugReportUrl: String,
        override val viewSourceUrl: String,
        override val termsConditionsUrl: String,
        override val version: Int,
        override val logger: PYDroidLogger?,
        internal val application: Application,
        internal val debug: DebugParameters,
        internal val theme: ComposeThemeProvider,
    ) : PYDroid.BaseParameters

    data class DebugParameters(
        internal val enabled: Boolean,
        internal val upgradeAvailable: Boolean,
    )
  }

  class ComponentImpl private constructor(params: Component.Parameters) : Component {

    private val context = params.application

    private val theming: Theming by lazy(LazyThreadSafetyMode.NONE) { ThemingImpl(preferences) }

    private val billingErrorBus by lazy(LazyThreadSafetyMode.NONE) { EventBus.create<Throwable>() }

    private val protection by
        lazy(LazyThreadSafetyMode.NONE) {
          Protection.create(
              licenseKey = params.googlePlayLicenseVerificationKey,
          )
        }

    private val imageLoader: coil.ImageLoader by
        lazy(LazyThreadSafetyMode.NONE) { params.imageLoader() }

    private val preferences by
        lazy(LazyThreadSafetyMode.NONE) {
          PYDroidPreferencesImpl(params.application, params.version)
        }

    private val composeTheme by
        lazy(LazyThreadSafetyMode.NONE) {
          ComposeThemeFactory(theming = theming, themeProvider = params.theme)
        }

    private val dataPolicyModule by
        lazy(LazyThreadSafetyMode.NONE) {
          DataPolicyModule(
              DataPolicyModule.Parameters(
                  context = context.applicationContext,
                  preferences = preferences,
              ),
          )
        }

    private val settingsModule by
        lazy(LazyThreadSafetyMode.NONE) {
          SettingsModule(
              SettingsModule.Parameters(context = context.applicationContext),
          )
        }

    private val aboutModule by lazy(LazyThreadSafetyMode.NONE) { AboutModule() }

    private val networkModule by
        lazy(LazyThreadSafetyMode.NONE) {
          NetworkModule(
              NetworkModule.Parameters(addLoggingInterceptor = params.debug.enabled),
          )
        }

    private val otherAppsModule by
        lazy(LazyThreadSafetyMode.NONE) {
          OtherAppsModule(
              OtherAppsModule.Parameters(
                  context = context.applicationContext,
                  packageName = context.applicationContext.packageName,
                  serviceCreator = networkModule.provideServiceCreator(),
              ),
          )
        }

    private val changeLogModule by
        lazy(LazyThreadSafetyMode.NONE) {
          ChangeLogModule(
              ChangeLogModule.Parameters(
                  context = context.applicationContext,
                  preferences = preferences,
              ),
          )
        }

    private val appParams by
        lazy(LazyThreadSafetyMode.NONE) {
          AppComponent.Factory.Parameters(
              context = context.applicationContext,
              theming = theming,
              billingErrorBus = billingErrorBus,
              changeLogModule = changeLogModule,
              composeTheme = composeTheme,
              imageLoader = imageLoader,
              protection = protection,
              version = params.version,
              isFakeUpgradeChecker = params.debug.enabled,
              isFakeUpgradeAvailable = params.debug.upgradeAvailable,
              isFake = params.debug.enabled,
              aboutModule = aboutModule,
              otherAppsModule = otherAppsModule,
              settingsModule = settingsModule,
              dataPolicyModule = dataPolicyModule,
              bugReportUrl = params.bugReportUrl,
              termsConditionsUrl = params.termsConditionsUrl,
              privacyPolicyUrl = params.privacyPolicyUrl,
              viewSourceUrl = params.viewSourceUrl,
          )
        }

    private val provider by
        lazy(LazyThreadSafetyMode.NONE) {
          object : ModuleProvider {

            private val modules by
                lazy(LazyThreadSafetyMode.NONE) {
                  object : ModuleProvider.Modules {
                    override fun theming(): Theming {
                      return theming
                    }
                  }
                }

            override fun get(): ModuleProvider.Modules {
              return modules
            }
          }
        }

    init {
      params.logger?.also { Logger.setLogger(it) }

      MainScope().launch(context = Dispatchers.Default) { theming.init() }
    }

    override fun plusApp(): AppComponent.Factory {
      return AppComponent.Impl.FactoryImpl(appParams)
    }

    override fun moduleProvider(): ModuleProvider {
      return provider
    }

    class FactoryImpl internal constructor() : Factory {

      override fun create(params: Component.Parameters): Component {
        return ComponentImpl(params)
      }
    }
  }
}
