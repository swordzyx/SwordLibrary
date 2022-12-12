package com.example.swordlibrary.webcontent

import android.app.Activity
import android.content.Intent
import android.net.Uri

fun openWebPageUrl(activity: Activity, webPageUrl: String) {
  try {
    openWebPageUri(activity, Uri.parse(webPageUrl))
  } catch (e:Exception) {
    e.printStackTrace()
  }
}

fun openWebPageUri(activity: Activity, webPageUri: Uri) {
  try {
    val intent = Intent(Intent.ACTION_VIEW, webPageUri)
    if (intent.resolveActivity(activity.packageManager) != null) {
      activity.startActivity(intent)
    }
  } catch (e: Exception) {
    e.printStackTrace()
  }
}