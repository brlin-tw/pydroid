/*
 *     Copyright (C) 2017 Peter Kenji Yamanaka
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.pyamsoft.pydroid.ui

import android.content.Context
import android.os.StrictMode
import android.support.annotation.CheckResult
import android.support.annotation.RestrictTo
import com.pyamsoft.pydroid.PYDroidModule
import com.pyamsoft.pydroid.ui.about.UiLicenses
import timber.log.Timber

object PYDroid {

  @RestrictTo(RestrictTo.Scope.LIBRARY) private var component: PYDroidComponent? = null
  @RestrictTo(RestrictTo.Scope.LIBRARY) private var debugMode = false

  @RestrictTo(
      RestrictTo.Scope.LIBRARY)
  @JvmStatic private fun guaranteeNonNull(): PYDroidComponent {
    val obj = component
    if (obj == null) {
      throw IllegalStateException("Component must undergo initialize(Context, Boolean) before use")
    } else {
      return obj
    }
  }

  /**
   * Return the DEBUG state of the library
   */
  @JvmStatic
  @CheckResult
  fun isDebugMode(): Boolean {
    guaranteeNonNull()
    return debugMode
  }

  /**
   * Initialize the library
   */
  @JvmOverloads
  @JvmStatic
  fun initialize(context: Context, debug: Boolean,
      allowReInitialize: Boolean = false) {
    debugMode = debug
    if (component == null || allowReInitialize) {
      component = PYDroidComponentImpl(PYDroidModule(context.applicationContext, debug))
      if (debug) {
        Timber.plant(Timber.DebugTree())
        setStrictMode()
      }
      UiLicenses.addLicenses()
    }
  }

  private fun setStrictMode() {
    StrictMode.setThreadPolicy(
        StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDeath().permitDiskReads().permitDiskWrites().penaltyFlashScreen().build())
    StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build())
  }

  /**
   * For use internally in the library
   */
  @RestrictTo(RestrictTo.Scope.LIBRARY)
  @JvmStatic internal fun with(
      func: (PYDroidComponent) -> Unit) {
    func(guaranteeNonNull())
  }
}
