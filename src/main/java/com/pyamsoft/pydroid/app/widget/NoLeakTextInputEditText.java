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

package com.pyamsoft.pydroid.app.widget;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

/**
 * Attempts to fix TextView memory leak
 *
 * https://github.com/square/leakcanary/issues/180
 */
public class NoLeakTextInputEditText extends TextInputEditText {

  public NoLeakTextInputEditText(Context context) {
    super(context);
  }

  public NoLeakTextInputEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public NoLeakTextInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override protected void onDetachedFromWindow() {
    getViewTreeObserver().removeOnPreDrawListener(this);
    super.onDetachedFromWindow();
  }
}
