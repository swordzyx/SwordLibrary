package com.sword.androidarticle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.kotlin_android_extension_activity.*

class KotlinAndroidExtensionActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		text.text = "hello"
	}

}