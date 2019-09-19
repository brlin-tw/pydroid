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

import android.text.SpannedString
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.TextView
import com.pyamsoft.pydroid.arch.BaseUiView
import com.pyamsoft.pydroid.arch.UiSavedState
import com.pyamsoft.pydroid.ui.R

internal class RatingChangelogView internal constructor(
    private val changelog: SpannedString,
    parent: ViewGroup
) : BaseUiView<RatingDialogViewState, RatingDialogViewEvent>(parent) {

    private val changelogText by boundView<TextView>(R.id.rating_changelog_text)

    override val layout: Int = R.layout.rating_changelog

    override val layoutRoot by boundView<ScrollView>(R.id.rating_changelog_scroll)

    init {
        doOnInflate {
            changelogText.text = changelog
        }

        doOnTeardown {
            changelogText.text = ""
        }
    }

    override fun onRender(
        state: RatingDialogViewState,
        savedState: UiSavedState
    ) {
    }
}
