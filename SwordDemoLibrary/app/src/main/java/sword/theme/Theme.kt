package sword.theme

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.example.swordlibrary.R
import sword.dp
import sword.dp2px

class Theme {
    companion object {
        fun createCircleButton(context: Context): AppCompatButton {
            return AppCompatButton(context).apply {
                background = ResourcesCompat.getDrawable(context.resources, R.drawable.background_circle_corner_blue, null)
                setPadding(20.dp, 10.dp, 20.dp, 10.dp)
                setTextColor(Color.WHITE)
                textSize = dp2px(10f)
                gravity = Gravity.CENTER
                foreground = null
            }
        }
        
        fun createEditText(context: Context): AppCompatEditText {
            return AppCompatEditText(context).apply { 
                textSize = dp2px(12f)
            }
        }
    }
}