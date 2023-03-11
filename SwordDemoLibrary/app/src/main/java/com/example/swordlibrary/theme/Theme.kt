package com.example.swordlibrary.theme

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.example.swordlibrary.R
import com.sword.dp
import com.sword.dp2px

class Theme {
    companion object {
        fun createCircleButton(context: Context): AppCompatButton {
            return AppCompatButton(context).apply {
                background = ResourcesCompat.getDrawable(context.resources, R.drawable.button_circle_background, null)
                setPadding(dp(20), dp(10), dp(20), dp(10))
                setTextColor(Color.WHITE)
                textSize = dp2px(10f)
                gravity = Gravity.CENTER
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    foreground = null
                }
            }
        }
        
        fun createEditText(context: Context): AppCompatEditText {
            return AppCompatEditText(context).apply { 
                textSize = dp2px(12f)
            }
        }
    }
}