/*
 * Copyright 2021 Peter Kenji Yamanaka
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

package com.pyamsoft.pydroid.loader.glide.loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.pyamsoft.pydroid.loader.glide.transform.GlideDrawableTransformer

@Deprecated("You almost always want to use something else.")
internal class GlideBitmapDrawableLoader internal constructor(
    context: Context,
    private val bitmap: Bitmap
) : GlideDrawableTransformer(context) {

    override fun onCreateRequest(builder: RequestBuilder<Drawable>): RequestBuilder<Drawable> {
        return builder.load(bitmap)
    }

    override fun mutateImage(resource: Drawable): Drawable {
        return resource.mutate()
    }

    override fun setImage(view: ImageView, image: Drawable) {
        view.setImageDrawable(image)
    }

    override fun immediateResource(): Drawable {
        return BitmapDrawable(context.resources, bitmap)
    }
}
