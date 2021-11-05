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

package com.pyamsoft.pydroid.ui.internal.billing.listitem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

internal class BillingAdapter
internal constructor(private val owner: LifecycleOwner, private val callback: Callback) :
    ListAdapter<BillingItemViewState, BillingViewHolder>(DIFFER) {

  init {
    setHasStableIds(true)
  }

  override fun getItemId(position: Int): Long {
    return getItem(position).sku.hashCode().toLong()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return BillingViewHolder.create(inflater, parent, owner, callback)
  }

  override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
    val item = getItem(position)
    holder.bindState(item)
  }

  interface Callback {

    fun onPurchase(index: Int)
  }

  companion object {

    private val DIFFER =
        object : DiffUtil.ItemCallback<BillingItemViewState>() {
          override fun areItemsTheSame(
              oldItem: BillingItemViewState,
              newItem: BillingItemViewState
          ): Boolean {
            return oldItem.sku.id == newItem.sku.id
          }

          override fun areContentsTheSame(
              oldItem: BillingItemViewState,
              newItem: BillingItemViewState
          ): Boolean {
            return oldItem == newItem
          }
        }
  }
}
