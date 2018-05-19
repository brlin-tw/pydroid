/*
 * Copyright (C) 2018 Peter Kenji Yamanaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pyamsoft.pydroid.presenter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Presenter<V : Any> protected constructor() : LifecycleObserver {

  private val pauseDisposables = CompositeDisposable()
  private val stopDisposables = CompositeDisposable()
  private val destroyDisposables = CompositeDisposable()
  private var lifecycle: Lifecycle? = null
  protected var view: V? = null
    private set

  fun bind(
    owner: LifecycleOwner,
    view: V
  ) {
    bind(owner.lifecycle, view)
  }

  fun bind(
    lifecycle: Lifecycle,
    view: V
  ) {
    this.view = view
    this.lifecycle = lifecycle
    lifecycle.addObserver(this)
  }

  @OnLifecycleEvent(ON_CREATE)
  internal fun performCreate() {
    onCreate()
  }

  protected open fun onCreate() {
  }

  @OnLifecycleEvent(ON_START)
  internal fun performStart() {
    onStart()
  }

  protected open fun onStart() {
  }

  @OnLifecycleEvent(ON_RESUME)
  internal fun performResume() {
    onResume()
  }

  protected open fun onResume() {
  }

  @OnLifecycleEvent(ON_PAUSE)
  internal fun performPause() {
    onPause()

    // Clear disposables after hook in case something accidentally subscribes
    pauseDisposables.clear()
  }

  protected open fun onPause() {
  }

  @OnLifecycleEvent(ON_STOP)
  internal fun performStop() {
    onStop()

    // Clear disposables after hook in case something accidentally subscribes
    pauseDisposables.clear()
    stopDisposables.clear()
  }

  protected open fun onStop() {
  }

  @OnLifecycleEvent(ON_DESTROY)
  internal fun performDestroy() {
    // Unbind the view
    this.view = null

    // Remove the lifecycle observer since we are dead
    lifecycle?.removeObserver(this)
    lifecycle = null

    onDestroy()

    // Clear disposables after hook in case something accidentally subscribes
    pauseDisposables.clear()
    stopDisposables.clear()
    destroyDisposables.clear()
  }

  protected open fun onDestroy() {
  }

  /**
   * Add a disposable to the internal list, dispose it onUnbind
   */
  @JvmOverloads
  protected inline fun dispose(
    event: Lifecycle.Event = ON_DESTROY,
    func: () -> Disposable
  ) {
    dispose(event, func())
  }

  /**
   * Add a disposable to the internal list, dispose it onUnbind
   */
  @JvmOverloads
  protected fun dispose(
    event: Lifecycle.Event = ON_DESTROY,
    disposable: Disposable
  ) {
    val disposables: CompositeDisposable = when (event) {
      ON_PAUSE -> pauseDisposables
      ON_STOP -> stopDisposables
      ON_DESTROY -> destroyDisposables
      else -> throw IllegalArgumentException("Unsupported event event: $event")
    }
    disposables.add(disposable)
  }

}
