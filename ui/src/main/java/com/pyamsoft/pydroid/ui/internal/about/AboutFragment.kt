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

package com.pyamsoft.pydroid.ui.internal.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CheckResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.pyamsoft.pydroid.arch.StateSaver
import com.pyamsoft.pydroid.arch.bindController
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.PYDroidComponent
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.arch.fromViewModelFactory
import com.pyamsoft.pydroid.ui.databinding.LayoutFrameBinding
import com.pyamsoft.pydroid.util.hyperlink

internal class AboutFragment : Fragment() {

    private var stateSaver: StateSaver? = null
    internal var listView: AboutListView? = null
    internal var errorView: AboutErrors? = null

    internal var factory: ViewModelProvider.Factory? = null
    private val viewModel by fromViewModelFactory<AboutViewModel>(activity = true) { factory }

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

        val binding = LayoutFrameBinding.bind(view)
        Injector.obtainFromApplication<PYDroidComponent>(view.context)
            .plusAbout()
            .create(binding.layoutFrame, viewLifecycleOwner)
            .inject(this)

        stateSaver = viewModel.bindController(
            savedInstanceState,
            viewLifecycleOwner,
            requireNotNull(listView),
            requireNotNull(errorView)
        ) { scope, event ->
            return@bindController when (event) {
                is AboutViewEvent.ErrorEvent.HideLoadError -> viewModel.handleClearLoadError(scope)
                is AboutViewEvent.ErrorEvent.HideNavigationError -> viewModel.handleHideNavigation(
                    scope
                )
                is AboutViewEvent.ListItemEvent.OpenLibrary -> viewModel.openLibrary(event.index) {
                    openUrl(it)
                }
                is AboutViewEvent.ListItemEvent.OpenLicense -> viewModel.openLicense(event.index) { url ->
                    openUrl(url)
                }
            }
        }

        viewModel.loadLicenses(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listView = null
        factory = null
        errorView = null
        stateSaver = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stateSaver?.saveState(outState)
    }

    private fun openUrl(url: String) {
        val scope = viewLifecycleOwner.lifecycleScope
        url.hyperlink(requireActivity()).navigate()
            .onSuccess { viewModel.navigationSuccess(scope) }
            .onFailure { viewModel.navigationFailed(scope, it) }
    }

    companion object {

        @JvmStatic
        @CheckResult
        internal fun newInstance(): Fragment {
            return AboutFragment().apply {
                arguments = Bundle().apply {}
            }
        }
    }
}
