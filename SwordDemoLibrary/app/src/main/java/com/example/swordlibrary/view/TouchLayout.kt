package com.example.swordlibrary.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

class TouchLayout(context: Context?, attributeSet: AttributeSet?) : ViewGroup(context, attributeSet) {
	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
		// TODO("Not yet implemented")
	}

	override fun shouldDelayChildPressedState(): Boolean {
		return false
	}
}