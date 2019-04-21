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

package com.pyamsoft.pydroid.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CheckResult
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.pyamsoft.pydroid.bootstrap.libraries.OssLibraries
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.PYDroidComponent
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.app.requireArguments
import com.pyamsoft.pydroid.ui.app.requireToolbarActivity
import com.pyamsoft.pydroid.ui.util.commit
import com.pyamsoft.pydroid.util.hyperlink

class AboutFragment : Fragment(), AboutUiComponent.Callback, AboutToolbarUiComponent.Callback {

  internal var _toolbarComponent: AboutToolbarUiComponent? = null
  private val toolbarComponent: AboutToolbarUiComponent
    get() = requireNotNull(_toolbarComponent)

  internal var _component: AboutUiComponent? = null
  private val component: AboutUiComponent
    get() = requireNotNull(_component)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.layout_frame, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    val backstack = requireArguments().getInt(KEY_BACK_STACK, 0)
    val layoutRoot = view.findViewById<FrameLayout>(R.id.layout_frame)
    Injector.obtain<PYDroidComponent>(view.context.applicationContext)
        .plusAbout()
        .create(viewLifecycleOwner, requireToolbarActivity(), backstack, layoutRoot)
        .inject(this)

    component.bind(viewLifecycleOwner, savedInstanceState, this)
    toolbarComponent.bind(viewLifecycleOwner, savedInstanceState, this)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _component = null
    _toolbarComponent = null
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    toolbarComponent.saveState(outState)
    component.saveState(outState)
  }

  override fun onNavigateExternalUrl(url: String) {
    val error = url.hyperlink(requireActivity())
        .navigate()
    if (error != null) {
      component.failedNavigation(error)
    }
  }

  override fun close() {
    requireActivity().onBackPressed()
  }

  companion object {

    private const val TAG = "AboutFragment"
    private const val KEY_BACK_STACK = "key_back_stack"

    @JvmStatic
    fun show(
      activity: FragmentActivity,
      @IdRes container: Int
    ) {
      // If you're using this function, all of these are available
      OssLibraries.BOOTSTRAP = true
      OssLibraries.UTIL = true
      OssLibraries.ARCH = true
      OssLibraries.LOADER = true
      OssLibraries.UI = true

      val fragmentManager = activity.supportFragmentManager
      val backStackCount = fragmentManager.backStackEntryCount
      if (fragmentManager.findFragmentByTag(TAG) == null) {
        fragmentManager.beginTransaction()
            .replace(container, newInstance(backStackCount), TAG)
            .addToBackStack(null)
            .commit(activity)
      }
    }

    @Suppress("unused")
    @JvmStatic
    @CheckResult
    fun isPresent(activity: FragmentActivity): Boolean =
      (activity.supportFragmentManager.findFragmentByTag(TAG) != null)

    @JvmStatic
    @CheckResult
    private fun newInstance(backStackCount: Int): AboutFragment {
      return AboutFragment().apply {
        arguments = Bundle().apply {
          putInt(KEY_BACK_STACK, backStackCount)
        }
      }
    }
  }
}
