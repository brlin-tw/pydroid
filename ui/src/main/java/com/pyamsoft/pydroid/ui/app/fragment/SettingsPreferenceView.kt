package com.pyamsoft.pydroid.ui.app.fragment

import com.pyamsoft.pydroid.util.HyperlinkIntent

interface SettingsPreferenceView {

  fun onMoreAppsClicked(onClick: () -> Unit)

  fun onFollowsClicked(
    onBlogClicked: (blogLink: HyperlinkIntent) -> Unit,
    onSocialClicked: (socialLink: HyperlinkIntent) -> Unit
  )

  fun onRateAppClicked(onClick: () -> Unit)

  fun onBugReportClicked(onClick: (report: HyperlinkIntent) -> Unit)

  fun onLicensesClicked(onClick: () -> Unit)

  fun onCheckVersionClicked(onClick: () -> Unit)

  fun onClearAllClicked(onClick: () -> Unit)

  fun onUpgradeClicked(onClick: () -> Unit)

}