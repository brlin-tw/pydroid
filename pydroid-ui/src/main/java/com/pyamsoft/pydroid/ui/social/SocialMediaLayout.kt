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

package com.pyamsoft.pydroid.ui.social

import android.content.ActivityNotFoundException
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.pyamsoft.pydroid.core.bus.Publisher
import com.pyamsoft.pydroid.loader.ImageLoader
import com.pyamsoft.pydroid.loader.targets.DrawableImageTarget
import com.pyamsoft.pydroid.ui.PYDroid
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.databinding.ViewSocialMediaBinding
import com.pyamsoft.pydroid.ui.util.setOnDebouncedClickListener

class SocialMediaLayout : LinearLayout, LifecycleOwner {

  internal lateinit var linker: Linker
  internal lateinit var linkerErrorPublisher: Publisher<ActivityNotFoundException>
  internal lateinit var imageLoader: ImageLoader
  private val binding: ViewSocialMediaBinding
  private val registry = LifecycleRegistry(this)

  constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int
  ) : super(context, attrs, defStyleAttr)

  constructor(
    context: Context,
    attrs: AttributeSet
  ) : super(context, attrs)

  constructor(context: Context) : super(context)

  init {
    orientation = HORIZONTAL
    binding = ViewSocialMediaBinding.inflate(LayoutInflater.from(context), this, false)
    addView(binding.root)
    PYDroid.obtain(context)
        .inject(this)
  }

  override fun getLifecycle(): Lifecycle {
    return registry
  }

  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    binding.apply {
      googlePlay.setOnDebouncedClickListener {
        linker.clickGooglePlay {
          linkerErrorPublisher.publish(it)
        }
      }
      googlePlus.setOnDebouncedClickListener {
        linker.clickGooglePlus {
          linkerErrorPublisher.publish(it)
        }
      }
      blogger.setOnDebouncedClickListener {
        linker.clickBlogger {
          linkerErrorPublisher.publish(it)
        }
      }
      facebook.setOnDebouncedClickListener {
        linker.clickFacebook {
          linkerErrorPublisher.publish(it)
        }
      }
    }

    registry.apply {
      handleLifecycleEvent(ON_CREATE)
      handleLifecycleEvent(ON_START)
      handleLifecycleEvent(ON_RESUME)
    }

    val self = this
    imageLoader.apply {
      fromResource(R.drawable.google_play).into(
          DrawableImageTarget.forImageView(binding.googlePlay)
      )
          .bind(self)
      fromResource(R.drawable.google_plus).into(
          DrawableImageTarget.forImageView(binding.googlePlus)
      )
          .bind(self)
      fromResource(R.drawable.blogger_icon).into(
          DrawableImageTarget.forImageView(binding.blogger)
      )
          .bind(self)
      fromResource(R.drawable.facebook_icon).into(
          DrawableImageTarget.forImageView(binding.facebook)
      )
          .bind(self)
    }
  }

  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    binding.apply {
      googlePlay.setOnDebouncedClickListener(null)
      googlePlus.setOnDebouncedClickListener(null)
      blogger.setOnDebouncedClickListener(null)
      facebook.setOnDebouncedClickListener(null)
    }

    registry.apply {
      handleLifecycleEvent(ON_PAUSE)
      handleLifecycleEvent(ON_STOP)
      handleLifecycleEvent(ON_DESTROY)
    }
  }
}
