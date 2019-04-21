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

package com.pyamsoft.pydroid.ui.rating.dialog

import android.app.Dialog
import android.os.Bundle
import android.text.SpannedString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.annotation.CheckResult
import androidx.fragment.app.DialogFragment
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.PYDroidComponent
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.app.noTitle
import com.pyamsoft.pydroid.ui.app.requireArguments
import com.pyamsoft.pydroid.ui.rating.ChangeLogProvider
import com.pyamsoft.pydroid.ui.util.MarketLinker

class RatingDialog : DialogFragment(), RatingDialogUiComponent.Callback {

  internal var component: RatingDialogUiComponent? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    isCancelable = false
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return super.onCreateDialog(savedInstanceState)
        .noTitle()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.layout_linear_vertical, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    val rateLink = requireArguments().getString(RATE_LINK, "")
    val changeLogIcon = requireArguments().getInt(CHANGE_LOG_ICON, 0)
    val changelog = requireArguments().getCharSequence(CHANGE_LOG_TEXT, "") as? SpannedString
    requireNotNull(rateLink)
    requireNotNull(changelog)
    require(rateLink.isNotBlank())
    require(changeLogIcon > 0)
    require(changelog.isNotBlank())

    val layoutRoot = view.findViewById<LinearLayout>(R.id.layout_linear_v)
    Injector.obtain<PYDroidComponent>(view.context.applicationContext)
        .plusRatingDialog()
        .create(rateLink, changeLogIcon, changelog, layoutRoot)
        .inject(this)

    requireNotNull(component).bind(viewLifecycleOwner, savedInstanceState, this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    component = null
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    requireNotNull(component).saveState(outState)
  }

  override fun onResume() {
    super.onResume()
    // The dialog is super small for some reason. We have to set the size manually, in onResume
    dialog.window?.setLayout(
        WindowManager.LayoutParams.MATCH_PARENT,
        WindowManager.LayoutParams.WRAP_CONTENT
    )
  }

  override fun onNavigateToApplicationPage(packageName: String) {
    val error = MarketLinker.linkToMarketPage(requireContext(), packageName)

    if (error != null) {
      requireNotNull(component).navigationFailed(error)
    }

    dismiss()
  }

  override fun onCancelRating() {
    dismiss()
  }

  companion object {

    internal const val TAG = "RatingDialog"
    private const val CHANGE_LOG_TEXT = "change_log_text"
    private const val CHANGE_LOG_ICON = "change_log_icon"
    private const val RATE_LINK = "rate_link"

    @CheckResult
    @JvmStatic
    fun newInstance(provider: ChangeLogProvider): RatingDialog {
      return RatingDialog().apply {
        arguments = Bundle().apply {
          putString(RATE_LINK, provider.getPackageName())
          putCharSequence(CHANGE_LOG_TEXT, provider.changelog)
          putInt(CHANGE_LOG_ICON, provider.applicationIcon)
        }
      }
    }
  }
}
