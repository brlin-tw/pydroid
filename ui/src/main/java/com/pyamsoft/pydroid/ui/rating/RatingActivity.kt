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

package com.pyamsoft.pydroid.ui.rating

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModelProvider
import com.pyamsoft.pydroid.arch.StateSaver
import com.pyamsoft.pydroid.arch.createComponent
import com.pyamsoft.pydroid.bootstrap.rating.AppReviewLauncher
import com.pyamsoft.pydroid.ui.Injector
import com.pyamsoft.pydroid.ui.PYDroidComponent
import com.pyamsoft.pydroid.ui.arch.viewModelFactory
import com.pyamsoft.pydroid.ui.internal.rating.RatingControllerEvent.LoadRating
import com.pyamsoft.pydroid.ui.internal.rating.RatingViewModel
import com.pyamsoft.pydroid.ui.version.VersionCheckActivity

abstract class RatingActivity : VersionCheckActivity() {

    private var stateSaver: StateSaver? = null

    internal var ratingFactory: ViewModelProvider.Factory? = null
    private val viewModel by viewModelFactory<RatingViewModel> { ratingFactory }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.obtain<PYDroidComponent>(applicationContext)
            .plusRating()
            .create()
            .inject(this)

        stateSaver = createComponent(
            savedInstanceState, this,
            viewModel
        ) {
            return@createComponent when (it) {
                is LoadRating -> showRating(it.launcher)
            }
        }
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        stateSaver?.saveState(outState)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        ratingFactory = null
        stateSaver = null
    }

    private fun showRating(launcher: AppReviewLauncher) {
        launcher.review(this)
    }
}
