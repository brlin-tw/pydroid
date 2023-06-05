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

android {
  namespace = "com.pyamsoft.pydroid.autopsy"

  kotlinOptions { freeCompilerArgs += "-Xexplicit-api=strict" }

  buildFeatures { compose = true }

  composeOptions { kotlinCompilerExtensionVersion = "${rootProject.extra["composeCompiler"]}" }
}

dependencies {
  // Compose
  implementation("androidx.activity:activity-compose:1.7.2")
  implementation("androidx.compose.ui:ui:${rootProject.extra["compose"]}")
  implementation("androidx.compose.material:material:${rootProject.extra["composeMaterial"]}")

  // Compose Preview
  compileOnly("androidx.compose.ui:ui-tooling-preview:${rootProject.extra["compose"]}")
  debugImplementation("androidx.compose.ui:ui-tooling:${rootProject.extra["compose"]}")

  // Accompanist
  implementation(
      "com.google.accompanist:accompanist-systemuicontroller:${rootProject.extra["accompanist"]}")

  implementation("androidx.startup:startup-runtime:1.1.1")

  api(project(":core"))
  api(project(":theme"))
}
