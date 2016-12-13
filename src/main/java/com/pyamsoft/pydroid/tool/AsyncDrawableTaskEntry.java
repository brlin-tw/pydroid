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

package com.pyamsoft.pydroid.tool;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

abstract class AsyncDrawableTaskEntry<T> extends AsyncTask<Activity, Void, T>
    implements AsyncMap.Entry {

  @Override public void unload() {
    cancel(true);
  }

  @Override public boolean isUnloaded() {
    return isCancelled();
  }
}
