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

package com.pyamsoft.pydroid.ui.util

import androidx.recyclerview.widget.RecyclerView
import com.pyamsoft.pydroid.arch.ViewBinder

/** Remove all item decorations from a recyclerview */
public fun RecyclerView.removeAllItemDecorations() {
  val totalCount = this.itemDecorationCount - 1
  if (totalCount <= 0) {
    return
  }

  for (i in totalCount..0) {
    this.removeItemDecorationAt(i)
  }
}

/** Call the ViewBinder.teardown() on all view holders */
public fun RecyclerView.Adapter<*>.teardownAdapter(recyclerView: RecyclerView) {
  val itemSize = itemCount
  if (itemSize <= 0) {
    return
  }

  for (index in 0 until itemSize) {
    val holder = recyclerView.findViewHolderForAdapterPosition(index)
    if (holder is ViewBinder<*>) {
      holder.teardown()
    }
  }
}
