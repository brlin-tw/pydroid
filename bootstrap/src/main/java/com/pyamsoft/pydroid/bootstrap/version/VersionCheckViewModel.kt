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

package com.pyamsoft.pydroid.bootstrap.version

import androidx.lifecycle.LifecycleOwner
import com.pyamsoft.pydroid.core.viewmodel.DataBus
import com.pyamsoft.pydroid.core.viewmodel.DataWrapper
import com.pyamsoft.pydroid.core.viewmodel.LifecycleViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class VersionCheckViewModel internal constructor(
  private val updateBus: DataBus<Int>,
  private val packageName: String,
  private val currentVersionCode: Int,
  private val interactor: VersionCheckInteractor
) : LifecycleViewModel {

  fun onUpdateAvailable(
    owner: LifecycleOwner,
    func: (DataWrapper<Int>) -> Unit
  ) {
    updateBus.listen()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(func)
        .bind(owner)
  }

  fun checkForUpdates(
    owner: LifecycleOwner,
    force: Boolean
  ) {
    interactor.checkVersion(force, packageName)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .filter { currentVersionCode < it }
        .doOnSubscribe { updateBus.publishLoading(force) }
        .doAfterTerminate { updateBus.publishComplete() }
        .subscribe({ updateBus.publishSuccess(it) }, {
          Timber.e(it, "Error checking for latest version")
          updateBus.publishError(it)
        })
        .disposeOnClear(owner)
  }
}
