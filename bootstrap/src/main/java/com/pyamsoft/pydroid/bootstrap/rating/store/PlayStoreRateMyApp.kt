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

package com.pyamsoft.pydroid.bootstrap.rating.store

import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.review.testing.FakeReviewManager
import com.pyamsoft.pydroid.bootstrap.rating.AppReviewLauncher
import com.pyamsoft.pydroid.bootstrap.rating.RateMyApp
import com.pyamsoft.pydroid.core.Enforcer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume

internal class PlayStoreRateMyApp internal constructor(
    context: Context,
    private val debug: Boolean
) : RateMyApp {

    private val manager by lazy {
        if (debug) {
            FakeReviewManager(context.applicationContext)
        } else {
            ReviewManagerFactory.create(context.applicationContext)
        }
    }

    override suspend fun startReview(): AppReviewLauncher =
        withContext(context = Dispatchers.IO) {
            Enforcer.assertOffMainThread()

            if (debug) {
                Timber.d("In debug mode we fake a delay to mimic real world network turnaround time.")
                delay(2000L)
            }

            return@withContext suspendCancellableCoroutine { continuation ->
                manager.requestReviewFlow()
                    .addOnFailureListener { error ->
                        Timber.e(error, "Failed to resolve app review info task")
                        continuation.resume(AppReviewLauncher.empty())
                    }
                    .addOnCompleteListener { request ->
                        Timber.d("App Review info received: $request")
                        if (request.isSuccessful) {
                            val info = request.result
                            continuation.resume(
                                PlayStoreAppReviewLauncher(
                                    manager,
                                    info,
                                )
                            )
                            return@addOnCompleteListener
                        }

                        Timber.d("Review is not available")
                        continuation.resume(AppReviewLauncher.empty())
                    }
            }
        }
}