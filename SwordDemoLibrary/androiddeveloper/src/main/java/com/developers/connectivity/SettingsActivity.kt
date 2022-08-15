package com.developers.connectivity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceActivity
import com.example.androiddeveloper.R

class SettingsActivity: PreferenceActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		addPreferencesFromResource(R.xml.preferences)
	}

	override fun onResume() {
		super.onResume()
		preferenceScreen?.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
	}

	override fun onPause() {
		super.onPause()
		preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
	}


	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
		TODO("Not yet implemented")
	}

}