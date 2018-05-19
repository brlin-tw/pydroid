/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.pydroid.ui.app.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.annotation.CheckResult
import androidx.fragment.app.DialogFragment
import com.pyamsoft.pydroid.ui.app.activity.ToolbarActivity

abstract class ToolbarDialog : DialogFragment(), ToolbarProvider {

  protected open val hasTitle: Boolean = false

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    if (!hasTitle) {
      dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    return dialog
  }

  final override val toolbarActivity: ToolbarActivity
    @get:CheckResult get() {
      val a = activity
      if (a is ToolbarActivity) {
        return a
      } else {
        throw ClassCastException("Activity does not implement ToolbarActivity")
      }
    }

  @CheckResult
  protected fun requireView(): View {
    return checkNotNull(view) { "View is required and cannot be null." }
  }

}
