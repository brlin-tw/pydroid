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

package com.pyamsoft.pydroid.loader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.annotation.CheckResult
import androidx.annotation.DrawableRes

/**
 * Generic ImageLoader
 *
 * Loads image resources in the background and loads data into ImageViews or other containers once
 * the image is ready
 */
public interface ImageLoader {

    /**
     * Load a drawable resource
     */
    @CheckResult
    public fun load(@DrawableRes resource: Int): Loader<Drawable>

    /**
     * Load a url resource
     */
    @CheckResult
    public fun load(uri: Uri): Loader<Drawable>

    /**
     * Load a url resource
     */
    @CheckResult
    public fun load(url: String): Loader<Drawable>

    /**
     * Load a byte array resource
     */
    @CheckResult
    public fun load(data: ByteArray): Loader<Bitmap>

    /**
     * Load a bitmap resource
     */
    @CheckResult
    public fun load(bitmap: Bitmap): Loader<Bitmap>
}
