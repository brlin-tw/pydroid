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
 *
 */

package com.pyamsoft.pydroid.notify

import android.content.Context
import androidx.annotation.CheckResult
import androidx.core.app.NotificationManagerCompat

class Notifier(private val dispatchers: Set<NotifyDispatcher<*>>, context: Context) {

    private val manager by lazy { NotificationManagerCompat.from(context.applicationContext) }

    @CheckResult
    fun <T : NotifyData> show(notification: T): NotifyId {
        return show(generateNotificationId(), NOTIFY_EMPTY_TAG, notification)
    }

    @CheckResult
    fun <T : NotifyData> show(id: NotifyId, notification: T): NotifyId {
        return show(id, NOTIFY_EMPTY_TAG, notification)
    }

    @CheckResult
    fun <T : NotifyData> show(tag: NotifyTag, notification: T): NotifyId {
        return show(generateNotificationId(), tag, notification)
    }

    @CheckResult
    fun <T : NotifyData> show(
        id: NotifyId,
        tag: NotifyTag,
        notification: T
    ): NotifyId {
        val dispatcher = dispatchers
            .asSequence()
            .filter { it.canShow(notification) }
            .map {
                // Unsafe cast but should be fine because of the above filter clause.
                // If the dispatcher canShow function returns false truths, then this will break
                // but that's on you.
                @Suppress("UNCHECKED_CAST")
                return@map it as? NotifyDispatcher<T>
            }
            .firstOrNull()
            ?: throw MissingDispatcherException(dispatchers, notification)

        val newNotification = dispatcher.build(id, notification)

        if (tag.tag.isNotBlank()) {
            manager.notify(tag.tag, id.id, newNotification)
        } else {
            manager.notify(id.id, newNotification)
        }

        return id
    }

    fun cancel(id: NotifyId) {
        cancel(id, NOTIFY_EMPTY_TAG)
    }

    fun cancel(
        id: NotifyId,
        tag: NotifyTag
    ) {
        if (tag.tag.isNotBlank()) {
            manager.cancel(tag.tag, id.id)
        } else {
            manager.cancel(id.id)
        }
    }

    companion object {

        private val NOTIFY_EMPTY_TAG = "".asNotifyTag()
        private val NOTIFICATION_ID_RANGE = (1000..50000)

        @CheckResult
        private fun generateNotificationId(): NotifyId {
            val rawId = NOTIFICATION_ID_RANGE.random()
            return rawId.asNotifyId()
        }
    }
}
