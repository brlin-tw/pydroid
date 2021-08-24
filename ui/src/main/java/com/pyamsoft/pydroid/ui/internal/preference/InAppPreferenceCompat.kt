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

/** Allows Preferences to use VectorDrawables as icons on API < 21 */
package com.pyamsoft.pydroid.ui.internal.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.PreferenceViewHolder
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.databinding.AdPreferenceLayoutBinding
import com.pyamsoft.pydroid.ui.preference.PreferenceCompat
import com.pyamsoft.pydroid.ui.preference.getStyledAttr
import androidx.preference.R as R2

internal class InAppPreferenceCompat : PreferenceCompat {

  constructor(context: Context) : this(context, null)

  constructor(
      context: Context,
      attrs: AttributeSet?
  ) : this(
      context,
      attrs,
      context.getStyledAttr(R2.attr.preferenceStyle, android.R.attr.preferenceStyle))

  constructor(
      context: Context,
      attrs: AttributeSet?,
      defStyleAttr: Int
  ) : this(context, attrs, defStyleAttr, 0)

  constructor(
      context: Context,
      attrs: AttributeSet?,
      defStyleAttr: Int,
      defStyleRes: Int
  ) : super(context, attrs, defStyleAttr, defStyleRes) {
    layoutResource = R.layout.inapp_preference_layout
  }

  override fun onBindViewHolder(holder: PreferenceViewHolder) {
    // Must run before super to fix view IDs
    val binding = AdPreferenceLayoutBinding.bind(holder.itemView)

    // Fix the IDs here
    // AAPT cannot build when we use @android:id/ inside of an XML layout because
    // of ViewBinding...
    binding.apply {
      adIcon.id = android.R.id.icon
      adSummary.id = android.R.id.summary
      adTitle.id = android.R.id.title
      adWidgetFrame.id = android.R.id.widget_frame
    }

    // Now run VH binding
    super.onBindViewHolder(holder)
  }
}
