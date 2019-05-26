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

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.pyamsoft.pydroid.arch.BaseUiView
import com.pyamsoft.pydroid.loader.ImageLoader
import com.pyamsoft.pydroid.loader.Loaded
import com.pyamsoft.pydroid.ui.R

internal class RatingIconView internal constructor(
  private val imageLoader: ImageLoader,
  parent: ViewGroup
) : BaseUiView<RatingDialogViewState, RatingDialogViewEvent>(parent) {

  private val iconView by boundView<ImageView>(R.id.icon)

  private var iconLoaded: Loaded? = null

  override val layout: Int = R.layout.rating_icon

  override val layoutRoot by boundView<View>(R.id.rating_icon_root)

  override fun onRender(
    state: RatingDialogViewState,
    savedInstanceState: Bundle?
  ) {
    state.changelogIcon.let { icon ->
      iconLoaded?.dispose()
      iconLoaded = imageLoader.load(icon)
          .into(iconView)
    }
  }

  override fun onTeardown() {
    iconLoaded?.dispose()
  }

}
