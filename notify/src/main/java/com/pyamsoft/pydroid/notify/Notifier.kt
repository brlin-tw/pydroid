/*
 * Copyright 2022 Peter Kenji Yamanaka
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

package com.pyamsoft.pydroid.notify

import android.app.Service
import android.content.Context
import androidx.annotation.CheckResult

/** Notifier manages various dispatchers and dispatches notification payloads to them */
public interface Notifier {

  /** Show a notification */
  @CheckResult
  public fun <T : NotifyData> show(channelInfo: NotifyChannelInfo, notification: T): NotifyId

  /** Show a notification with a given id */
  @CheckResult
  public fun <T : NotifyData> show(
      id: NotifyId,
      channelInfo: NotifyChannelInfo,
      notification: T
  ): NotifyId

  /** Show a notification with a given tag */
  @CheckResult
  public fun <T : NotifyData> show(
      tag: NotifyTag,
      channelInfo: NotifyChannelInfo,
      notification: T
  ): NotifyId

  /** Show a notification with a given id and tag */
  @CheckResult
  public fun <T : NotifyData> show(
      id: NotifyId,
      tag: NotifyTag,
      channelInfo: NotifyChannelInfo,
      notification: T
  ): NotifyId

  /** Cancel a notification by id */
  public fun cancel(id: NotifyId)

  /** Cancel a notification by id and tag */
  public fun cancel(id: NotifyId, tag: NotifyTag)

  /** Show a foreground notification */
  @CheckResult
  public fun <T : NotifyData> startForeground(
      service: Service,
      channelInfo: NotifyChannelInfo,
      notification: T
  ): NotifyId

  /** Show a foreground notification with a given id and tag */
  @CheckResult
  public fun <T : NotifyData> startForeground(
      service: Service,
      id: NotifyId,
      channelInfo: NotifyChannelInfo,
      notification: T
  ): NotifyId

  /** Cancel a foreground notification by id */
  public fun stopForeground(service: Service, id: NotifyId)

  /** Cancel a foreground notification by id and tag */
  public fun stopForeground(service: Service, id: NotifyId, tag: NotifyTag)

  public companion object {

    /** Create a new instance of a default Notifier */
    @CheckResult
    public fun createDefault(context: Context, dispatchers: Set<NotifyDispatcher<*>>): Notifier {
      return DefaultNotifier(context = context.applicationContext, dispatchers = dispatchers)
    }
  }
}
