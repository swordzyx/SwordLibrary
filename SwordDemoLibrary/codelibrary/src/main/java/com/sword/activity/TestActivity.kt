package com.sword.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sword.view.MultiTouchView

class TestActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val container = LinearLayout(this)
		setContentView(container)

		container.addView(MultiTouchView(this, null))
	}
}