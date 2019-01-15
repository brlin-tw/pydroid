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

package com.pyamsoft.pydroid.ui.widget.shadow

import android.os.Bundle
import android.view.ViewGroup
import com.pyamsoft.pydroid.ui.arch.UiView
import com.pyamsoft.pydroid.ui.databinding.DropshadowBinding

class DropshadowView internal constructor(private val parent: ViewGroup) : UiView {

  private lateinit var binding: DropshadowBinding

  override fun id(): Int {
    return binding.layoutRoot.id
  }

  override fun inflate(savedInstanceState: Bundle?) {
    binding = DropshadowBinding.inflate(parent.inflater(), parent, false)
    parent.addView(binding.root)
  }

  override fun saveState(outState: Bundle) {
  }

}