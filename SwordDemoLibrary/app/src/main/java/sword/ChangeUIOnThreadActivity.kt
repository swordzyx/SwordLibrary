package sword

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.R
import kotlin.concurrent.thread


class ChangeUIOnThreadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_ui_on_thread)

        //changeUiOnThreadOnCreate()
        changeUiOnThreadAfter2s()
    }

    //在 onCreate 方法中更新 UI，不会报错
    private fun changeUiOnThreadOnCreate() {
        val textView = findViewById<TextView>(R.id.textView)
        thread {
            textView.text = "onCreate"
        }
    }

    //2s 之后在子线程更新 UI,
    //todo：我觉得应该报错的，但实际上没有报错，为什么？
    private fun changeUiOnThreadAfter2s() {
        val textView = findViewById<TextView>(R.id.textView)
        thread {
            Thread.sleep(2000)
            textView.text = "onCreate"
        }
    }
}