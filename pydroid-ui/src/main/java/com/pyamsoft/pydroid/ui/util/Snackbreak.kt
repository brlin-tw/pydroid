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

package com.pyamsoft.pydroid.ui.util

import android.app.Dialog
import android.os.Bundle
import android.support.annotation.CheckResult
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.view.View

object Snackbreak {

  @JvmStatic
  fun short(
    activity: FragmentActivity,
    view: View,
    detail: ErrorDetail,
    message: CharSequence = "An unexpected error occurred"
  ) {
    withDetail(
        activity, view, Snackbar.LENGTH_SHORT, detail, message
    )
  }

  @JvmStatic
  fun long(
    activity: FragmentActivity,
    view: View,
    detail: ErrorDetail,
    message: CharSequence = "An unexpected error occurred"
  ) {
    withDetail(
        activity, view, Snackbar.LENGTH_LONG, detail, message
    )
  }

  @JvmStatic
  fun indefinite(
    activity: FragmentActivity,
    view: View,
    detail: ErrorDetail,
    message: CharSequence = "An unexpected error occurred"
  ) {
    withDetail(
        activity, view, Snackbar.LENGTH_INDEFINITE, detail, message
    )
  }

  @JvmStatic
  private fun withDetail(
    activity: FragmentActivity,
    view: View,
    duration: Int,
    detail: ErrorDetail,
    message: CharSequence
  ) {
    val snackbar = Snackbar.make(view, message, duration)
    snackbar.setAction("Details", DebouncedOnClickListener.create {
      DetailDialogFragment.newInstance(detail)
          .show(activity, "snackbreak_detail_dialog")
    })
  }

  data class ErrorDetail(
    val title: CharSequence = "",
    val message: CharSequence
  )

  internal class DetailDialogFragment : DialogFragment() {

    private lateinit var errorTitle: CharSequence
    private lateinit var errorMessage: CharSequence

    override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      arguments!!.also {
        errorTitle = it.getCharSequence(
            KEY_ERROR_TITLE
        ) ?: ""
        errorMessage = it.getCharSequence(
            KEY_ERROR_MESSAGE
        ) ?: "An unexpected error occurred."
      }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      return AlertDialog.Builder(requireActivity())
          .apply {
            if (errorTitle.isNotBlank()) {
              setTitle(errorTitle)
            }
            setMessage(errorMessage)
            setPositiveButton("Okay") { _, _ -> dismiss() }
          }
          .create()
    }

    companion object {

      private const val KEY_ERROR_TITLE = "error_title"
      private const val KEY_ERROR_MESSAGE = "error_message"

      @JvmStatic
      @CheckResult
      internal fun newInstance(detail: ErrorDetail): DetailDialogFragment {
        return DetailDialogFragment()
            .apply {
          arguments = Bundle().apply {
            putCharSequence(
                KEY_ERROR_TITLE, detail.title)
            putCharSequence(
                KEY_ERROR_MESSAGE, detail.message)
          }
        }
      }
    }
  }

}