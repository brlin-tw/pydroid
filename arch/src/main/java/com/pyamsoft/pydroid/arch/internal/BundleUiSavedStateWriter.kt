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

package com.pyamsoft.pydroid.arch.internal

import android.os.Binder
import android.os.Bundle
import android.os.Parcelable
import android.util.Size
import android.util.SizeF
import com.pyamsoft.pydroid.arch.UiSavedStateWriter
import java.io.Serializable

/** Bundle backed implementation of a UiSavedStateReader */
@Deprecated("Start migrating over to registerSaveState")
@PublishedApi
internal class BundleUiSavedStateWriter
@PublishedApi
internal constructor(private val bundle: Bundle) : UiSavedStateWriter {

  override fun <T : Any> put(key: String, value: T) {
    // Pulled from core-ktx bundleOf
    bundle.apply {
      when (value) {
        // Scalars
        is Boolean -> putBoolean(key, value)
        is Byte -> putByte(key, value)
        is Char -> putChar(key, value)
        is Double -> putDouble(key, value)
        is Float -> putFloat(key, value)
        is Int -> putInt(key, value)
        is Long -> putLong(key, value)
        is Short -> putShort(key, value)

        // References
        is Bundle -> putBundle(key, value)
        is CharSequence -> putCharSequence(key, value)
        is Parcelable -> putParcelable(key, value)

        // Scalar arrays
        is BooleanArray -> putBooleanArray(key, value)
        is ByteArray -> putByteArray(key, value)
        is CharArray -> putCharArray(key, value)
        is DoubleArray -> putDoubleArray(key, value)
        is FloatArray -> putFloatArray(key, value)
        is IntArray -> putIntArray(key, value)
        is LongArray -> putLongArray(key, value)
        is ShortArray -> putShortArray(key, value)

        // Reference arrays
        is Array<*> -> {
          val componentType = value::class.java.componentType!!
          @Suppress("UNCHECKED_CAST") // Checked by reflection.
          when {
            Parcelable::class.java.isAssignableFrom(componentType) -> {
              putParcelableArray(key, value as Array<Parcelable>)
            }
            String::class.java.isAssignableFrom(componentType) -> {
              putStringArray(key, value as Array<String>)
            }
            CharSequence::class.java.isAssignableFrom(componentType) -> {
              putCharSequenceArray(key, value as Array<CharSequence>)
            }
            Serializable::class.java.isAssignableFrom(componentType) -> {
              putSerializable(key, value)
            }
            else -> {
              val valueType = componentType.canonicalName
              throw IllegalArgumentException("Illegal value array type $valueType for key \"$key\"")
            }
          }
        }

        // Last resort. Also we must check this after Array<*> as all arrays are serializable.
        is Serializable -> putSerializable(key, value)
        is Binder -> {
          putBinder(key, value)
        }
        is Size -> {
          putSize(key, value)
        }
        is SizeF -> {
          putSizeF(key, value)
        }
        else -> {
          val valueType = value.javaClass.canonicalName
          throw IllegalArgumentException("Illegal value type $valueType for key \"$key\"")
        }
      }
    }
  }

  override fun <T : Any> remove(key: String): T? {
    // This is Deprecated but it still works, and no other API really replaces it
    @Suppress("DEPRECATION") val storedValue: Any? = bundle.get(key)
    if (storedValue != null) {
      bundle.remove(key)
    }

    @Suppress("UNCHECKED_CAST") return storedValue as? T
  }
}
