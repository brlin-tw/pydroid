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

package com.pyamsoft.pydroid.arch

import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.annotation.UiThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyamsoft.pydroid.core.Enforcer
import kotlinx.coroutines.Job

/**
 * A ViewModel implementation which models a single state object.
 *
 * Access the current state via withState and manipulate it via setState.
 * These calls are asynchronous.
 */
public abstract class UiStateViewModel<S : UiViewState> protected constructor(
    initialState: S,
) : ViewModel() {

    @Suppress("UNUSED_PARAMETER")
    @Deprecated(
        "\"debug\" parameter will be removed soon. Instead of a debug check to determine whether to run extra debug code, the debug check and code are removed via ProGuard rules. Be sure to assemble your release builds using ProGuard minification.",
        replaceWith = ReplaceWith("UiStateViewModel<S>(initialState)")
    )
    protected constructor(initialState: S, debug: Boolean) : this(initialState)

    private val delegate = UiStateModel(initialState, viewModelScope)

    /**
     * The current state
     *
     * The StateFlow implementation should be thread/coroutine safe, so this can be called from anywhere.
     */
    protected val state: S
        @get:CheckResult get() {
            return delegate.state
        }

    /**
     * Bind renderables to this ViewModel.
     *
     * Once bound, any changes to the ViewModel.state will be sent to these renderables.
     */
    @UiThread
    @CheckResult
    public fun bind(vararg renderables: Renderable<S>): Job {
        return delegate.bind(*renderables)
    }

    /**
     * Bind a renderable to this ViewModel.
     *
     * Once bound, any changes to the ViewModel.state will be sent to this renderable.
     */
    @UiThread
    @CheckResult
    public inline fun bind(crossinline onRender: (UiRender<S>) -> Unit): Job {
        return bind(Renderable { onRender(it) })
    }

    // internal instead of protected so that only callers in the module can use this
    internal suspend fun bindState(renderables: Array<out Renderable<S>>) {
        delegate.bindState(renderables)
    }

    /**
     * Modify the state from the previous
     *
     * Note that, like calling this.setState() in React, this operation does not happen immediately.
     */
    protected fun setState(stateChange: S.() -> S) {
        delegate.setState(stateChange)
    }

    /**
     * Modify the state from the previous
     *
     * Note that, like calling this.setState() in React, this operation does not happen immediately.
     *
     * The andThen callback will be fired after the state has changed and the view has been notified.
     * If the stateChange payload does not cause a state update, the andThen call will not be fired.
     *
     * There is no threading guarantee for the andThen callback
     */
    protected fun setState(stateChange: S.() -> S, andThen: suspend (newState: S) -> Unit) {
        delegate.setState(stateChange, andThen)
    }

    /**
     * Act upon the current state
     *
     * Note that like accessing state in React using this.state.<var>, this is not immediate and
     * may not be up to date with the latest setState() call.
     */
    @Deprecated("Use the state variable directly to access the current state. To access state after a setState call, chain a follow up andThen() call")
    protected fun withState(func: S.() -> Unit) {
        delegate.withState(func)
    }

    /**
     * Clear the view model
     */
    @CallSuper
    override fun onCleared() {
        super.onCleared()
        Enforcer.assertOnMainThread()

        delegate.clear()
    }

}
