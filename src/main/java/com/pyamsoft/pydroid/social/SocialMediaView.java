/*
 * Copyright 2016 Peter Kenji Yamanaka
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

package com.pyamsoft.pydroid.social;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.pyamsoft.pydroid.R;
import com.pyamsoft.pydroid.SocialMediaLoaderCallback;
import com.pyamsoft.pydroid.databinding.ViewSocialMediaBinding;
import com.pyamsoft.pydroid.util.NetworkUtil;
import com.pyamsoft.pydroid.util.PersistentCache;

public class SocialMediaView extends FrameLayout implements SocialMediaPresenter.View {

  @NonNull private static final String KEY_PRESENTER = "key_social_presenter";
  @SuppressWarnings("WeakerAccess") SocialMediaPresenter presenter;
  private ViewSocialMediaBinding binding;
  private long loadedKey;

  public SocialMediaView(Context context) {
    super(context);
    init(context);
  }

  public SocialMediaView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public SocialMediaView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  public SocialMediaView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context);
  }

  private void init(@NonNull Context context) {
    binding = ViewSocialMediaBinding.bind(inflate(context, R.layout.view_social_media, this));

    loadedKey = PersistentCache.get().load(KEY_PRESENTER, null, new SocialMediaLoaderCallback() {

      @Override public void onPersistentLoaded(@NonNull SocialMediaPresenter persist) {
        presenter = persist;
      }
    });
  }

  @Override protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    binding.googlePlay.setOnClickListener(null);
    binding.blogger.setOnClickListener(null);
    binding.googlePlus.setOnClickListener(null);
    binding.facebook.setOnClickListener(null);

    binding.unbind();
    presenter.unbindView();
    PersistentCache.get().unload(loadedKey);
  }

  @Override protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    binding.googlePlay.setOnClickListener(v -> presenter.clickGooglePlay());
    binding.blogger.setOnClickListener(v -> presenter.clickBlogger());
    binding.googlePlus.setOnClickListener(v -> presenter.clickGooglePlus());
    binding.facebook.setOnClickListener(v -> presenter.clickFacebook());
    presenter.bindView(this);
  }

  @Override public void onSocialMediaClicked(@NonNull String link) {
    NetworkUtil.newLink(getContext(), link);
  }
}
