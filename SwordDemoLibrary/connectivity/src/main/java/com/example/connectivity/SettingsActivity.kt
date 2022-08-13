package com.example.connectivity

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceActivity
import com.sword.LogUtil

private const val TAG = "Connectivity" 

class SettingsActivity : PreferenceActivity() {
  
  private val sharePreferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key -> 
    LogUtil.debug("$key changed")
    sharedPreferences.all.forEach {
      LogUtil.debug(TAG, "read all sharedPreference, ${it.key}-${it.value}")
    }
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    addPreferencesFromResource(R.xml.preferences)
  }

  override fun onResume() {
    super.onResume()
    preferenceScreen?.sharedPreferences?.registerOnSharedPreferenceChangeListener(sharePreferenceChangeListener)
  }

  override fun onPause() {
    super.onPause()
    preferenceScreen?.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(sharePreferenceChangeListener)
  }
}