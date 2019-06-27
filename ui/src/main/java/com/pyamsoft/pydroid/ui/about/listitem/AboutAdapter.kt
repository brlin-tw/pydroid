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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.pyamsoft.pydroid.bootstrap.libraries.OssLibrary
import kotlinx.coroutines.ExperimentalCoroutinesApi

internal class AboutAdapter internal constructor(
  private val callback: (event: AboutItemControllerEvent) -> Unit
) : ListAdapter<OssLibrary, BaseViewHolder>(object : DiffUtil.ItemCallback<OssLibrary>() {
  override fun areItemsTheSame(
    oldItem: OssLibrary,
    newItem: OssLibrary
  ): Boolean {
    return oldItem.libraryUrl == newItem.libraryUrl
  }

  override fun areContentsTheSame(
    oldItem: OssLibrary,
    newItem: OssLibrary
  ): Boolean {
    return oldItem == newItem
  }

}) {

  init {
    setHasStableIds(true)
  }

  override fun getItemId(position: Int): Long {
    return getItem(position).libraryUrl.hashCode()
        .toLong()
  }

  override fun getItemViewType(position: Int): Int {
    if (getItem(position).name.isBlank()) {
      return VIEW_TYPE_SPACER
    } else {
      return VIEW_TYPE_REAL
    }
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): BaseViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    if (viewType == VIEW_TYPE_REAL) {
      return AboutViewHolder.create(inflater, parent, callback)
    } else {
      return SpaceViewHolder.create(inflater, parent)
    }
  }

  override fun onBindViewHolder(
    holder: BaseViewHolder,
    position: Int
  ) {
    holder.bind(getItem(position))
  }

  override fun onViewRecycled(holder: BaseViewHolder) {
    super.onViewRecycled(holder)
    holder.unbind()
  }

  companion object {

    private const val VIEW_TYPE_SPACER = 1
    private const val VIEW_TYPE_REAL = 2
  }
}
