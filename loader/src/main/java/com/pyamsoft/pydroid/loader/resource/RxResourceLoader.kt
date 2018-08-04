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

package com.pyamsoft.pydroid.loader.resource

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.pyamsoft.pydroid.core.threads.Enforcer
import com.pyamsoft.pydroid.loader.cache.ImageCache
import com.pyamsoft.pydroid.loader.loaded.Loaded
import com.pyamsoft.pydroid.loader.loaded.RxLoaded
import com.pyamsoft.pydroid.loader.targets.Target
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

internal class RxResourceLoader internal constructor(
  enforcer: Enforcer, context: Context,
  @DrawableRes resource: Int, @DrawableRes errorResource: Int,
  resourceImageCache: ImageCache<Int, Drawable>
) : ResourceLoader(enforcer, context, resource, errorResource, resourceImageCache) {

  override fun load(target: Target<Drawable>, @DrawableRes resource: Int): Loaded {
    return RxLoaded(
        Single.fromCallable { loadResource() }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { startAction?.invoke() }
            .doAfterSuccess { completeAction?.invoke(it) }
            .doOnError {
              Timber.e(it, "Error loading Drawable using RxResourceLoader")
              errorAction?.invoke(it)
            }.subscribe({ target.loadImage(it) }, { target.loadError(loadErrorResource()) })
    )
  }
}
