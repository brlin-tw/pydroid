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

package com.pyamsoft.pydroid.ui.about.listitem

import android.view.ViewGroup
import androidx.annotation.CheckResult
import com.pyamsoft.pydroid.bootstrap.libraries.OssLibrary
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal interface AboutItemComponent {

  fun inject(viewHolder: AboutViewHolder)

  interface Factory {

    @CheckResult
    fun create(
      parent: ViewGroup,
      library: OssLibrary
    ): AboutItemComponent

  }

  class Impl private constructor(
    private val parent: ViewGroup,
    private val library: OssLibrary
  ) : AboutItemComponent {

    override fun inject(viewHolder: AboutViewHolder) {
      val viewModel = AboutItemViewModel()
      val title = AboutItemTitleView(library, parent)
      val description = AboutItemDescriptionView(library, parent)
      val action = AboutItemActionView(library, parent)

      viewHolder.viewModel = viewModel
      viewHolder.titleView = title
      viewHolder.descriptionView = description
      viewHolder.actionView = action
    }

    class FactoryImpl internal constructor(
    ) : Factory {

      override fun create(
        parent: ViewGroup,
        library: OssLibrary
      ): AboutItemComponent {
        return Impl(parent, library)
      }

    }

  }

}
