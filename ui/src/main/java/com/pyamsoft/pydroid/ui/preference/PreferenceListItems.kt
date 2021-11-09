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

package com.pyamsoft.pydroid.ui.preference

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AlertDialog
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.pyamsoft.pydroid.ui.R
import com.pyamsoft.pydroid.ui.internal.app.AdBadge
import com.pyamsoft.pydroid.ui.internal.app.InAppBadge

@Composable
internal fun SimplePreferenceItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    preference: Preferences.SimplePreference,
) {
  val isEnabled = preference.isEnabled
  val name = preference.name
  val summary = preference.summary
  val icon = preference.icon
  val onClick = preference.onClick

  PreferenceItem(
      isEnabled = isEnabled,
      text = name,
      summary = summary,
      icon = icon,
      imageLoader = imageLoader,
      modifier = { enabled -> modifier.clickable(enabled = enabled) { onClick?.invoke() } },
  )
}

@Composable
internal fun CustomPreferenceItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    preference: Preferences.CustomPreference,
) {
  val isEnabled = preference.isEnabled
  val name = preference.name
  val summary = preference.summary
  val icon = preference.icon
  val content = preference.content

  PreferenceItem(
      isEnabled = isEnabled,
      text = name,
      summary = summary,
      icon = icon,
      badge = { AdBadge() },
      imageLoader = imageLoader,
      modifier = { modifier },
      customContent = content,
  )
}

@Composable
internal fun AdPreferenceItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    preference: Preferences.AdPreference,
) {
  val isEnabled = preference.isEnabled
  val name = preference.name
  val summary = preference.summary
  val icon = preference.icon
  val onClick = preference.onClick

  PreferenceItem(
      isEnabled = isEnabled,
      text = name,
      summary = summary,
      icon = icon,
      badge = { AdBadge() },
      imageLoader = imageLoader,
      modifier = { enabled -> modifier.clickable(enabled = enabled) { onClick?.invoke() } },
  )
}

@Composable
internal fun InAppPreferenceItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    preference: Preferences.InAppPreference,
) {
  val isEnabled = preference.isEnabled
  val name = preference.name
  val summary = preference.summary
  val icon = preference.icon
  val onClick = preference.onClick

  PreferenceItem(
      isEnabled = isEnabled,
      text = name,
      summary = summary,
      icon = icon,
      badge = { InAppBadge() },
      imageLoader = imageLoader,
      modifier = { enabled -> modifier.clickable(enabled = enabled) { onClick?.invoke() } },
  )
}

@Composable
internal fun CheckBoxPreferenceItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    preference: Preferences.CheckBoxPreference,
) {
  val isEnabled = preference.isEnabled
  val name = preference.name
  val summary = preference.summary
  val icon = preference.icon
  val onCheckedChanged = preference.onCheckedChanged
  val checked = preference.checked

  PreferenceItem(
      isEnabled = isEnabled,
      text = name,
      summary = summary,
      icon = icon,
      trailing = { enabled ->
        Checkbox(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChanged,
        )
      },
      imageLoader = imageLoader,
      modifier = { enabled ->
        modifier.clickable(enabled = enabled) { onCheckedChanged(!checked) }
      },
  )
}

@Composable
internal fun SwitchPreferenceItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    preference: Preferences.SwitchPreference,
) {
  val isEnabled = preference.isEnabled
  val name = preference.name
  val summary = preference.summary
  val icon = preference.icon
  val onCheckedChanged = preference.onCheckedChanged
  val checked = preference.checked

  PreferenceItem(
      isEnabled = isEnabled,
      text = name,
      summary = summary,
      icon = icon,
      trailing = { enabled ->
        Switch(
            checked = checked,
            enabled = enabled,
            onCheckedChange = onCheckedChanged,
        )
      },
      imageLoader = imageLoader,
      modifier = { enabled ->
        modifier.clickable(enabled = enabled) { onCheckedChanged(!checked) }
      },
  )
}

@Composable
internal fun ListPreferenceItem(
    modifier: Modifier = Modifier,
    imageLoader: ImageLoader,
    preference: Preferences.ListPreference,
) {
  val (isDialogShown, showDialog) = remember { mutableStateOf(false) }

  val isEnabled = preference.isEnabled
  val title = preference.name
  val summary = preference.summary
  val icon = preference.icon
  val currentValue = preference.value
  val entries = preference.entries
  val onPreferenceSelected = preference.onPreferenceSelected

  PreferenceItem(
      isEnabled = isEnabled,
      text = title,
      summary = summary,
      icon = icon,
      imageLoader = imageLoader,
      modifier = { enabled ->
        modifier.clickable(enabled = enabled) { showDialog(!isDialogShown) }
      },
  )

  if (isDialogShown) {
    AlertDialog(
        onDismissRequest = { showDialog(false) },
        title = {
          Text(
              text = title,
              style = MaterialTheme.typography.h6,
          )
        },
        buttons = {
          Column(
              modifier = Modifier.padding(16.dp),
          ) {
            entries.forEach { current ->
              val name = current.key
              val value = current.value
              val isSelected = value == currentValue
              val onEntrySelected = {
                onPreferenceSelected(name, value)
                showDialog(false)
              }

              Row(
                  modifier =
                      Modifier.fillMaxWidth()
                          .selectable(
                              selected = isSelected,
                              onClick = {
                                if (!isSelected) {
                                  onEntrySelected()
                                }
                              },
                          )
                          .padding(16.dp),
                  verticalAlignment = Alignment.CenterVertically,
              ) {
                RadioButton(
                    modifier = Modifier.padding(end = 16.dp),
                    selected = isSelected,
                    onClick = {
                      if (!isSelected) {
                        onEntrySelected()
                      }
                    },
                )
                Text(
                    text = name,
                    style = MaterialTheme.typography.body1,
                )
              }
            }

            Row(
                modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
            ) {
              Spacer(
                  modifier = Modifier.weight(1F),
              )

              TextButton(
                  onClick = { showDialog(false) },
              ) {
                Text(
                    text = stringResource(R.string.close),
                )
              }
            }
          }
        },
    )
  }
}

@Composable
private fun PreferenceItem(
    isEnabled: Boolean,
    imageLoader: ImageLoader,
    text: String,
    summary: String,
    @DrawableRes icon: Int,
    modifier: (isEnabled: Boolean) -> Modifier,
    trailing: (@Composable (isEnabled: Boolean) -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
    customContent: (@Composable (isEnabled: Boolean) -> Unit)? = null,
) {
  val enabled = LocalPreferenceEnabledStatus.current && isEnabled

  PreferenceAlphaWrapper(
      isEnabled = enabled,
  ) {
    if (customContent == null) {
      DefaultPreferenceItem(
          enabled = enabled,
          imageLoader = imageLoader,
          text = text,
          summary = summary,
          icon = icon,
          modifier = modifier,
          trailing = trailing,
          badge = badge,
      )
    } else {
      customContent(enabled)
    }
  }
}

@Composable
private fun DefaultPreferenceItem(
    enabled: Boolean,
    imageLoader: ImageLoader,
    text: String,
    summary: String,
    @DrawableRes icon: Int,
    modifier: (isEnabled: Boolean) -> Modifier,
    trailing: (@Composable (isEnabled: Boolean) -> Unit)? = null,
    badge: (@Composable () -> Unit)? = null,
) {
  Row(
      modifier = modifier(enabled).padding(8.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start,
  ) {
    Box(
        modifier = Modifier.size(48.dp),
        contentAlignment = Alignment.Center,
    ) {
      if (icon != 0) {
        val imageTintColor = if (MaterialTheme.colors.isLight) Color.Black else Color.White
        Image(
            modifier = Modifier.size(24.dp),
            painter =
                rememberImagePainter(
                    data = icon,
                    imageLoader = imageLoader,
                    builder = { crossfade(true) },
                ),
            contentDescription = null,
            colorFilter = ColorFilter.tint(color = imageTintColor),
        )
      }
    }

    Column(
        modifier = Modifier.padding(start = 8.dp).weight(1F),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
      Row(
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
        )
        badge?.let { compose -> Box(modifier = Modifier.padding(start = 8.dp)) { compose() } }
        Spacer(modifier = Modifier.weight(1F))
      }

      if (summary.isNotBlank()) {
        Box(
            modifier =
                Modifier.padding(
                    top = 8.dp,
                ),
        ) {
          Text(
              text = summary,
              style = MaterialTheme.typography.caption,
          )
        }
      }
    }

    trailing?.also { compose ->
      Box(
          modifier = Modifier.padding(start = 8.dp).size(48.dp),
          contentAlignment = Alignment.Center,
      ) { compose(enabled) }
    }
  }
}