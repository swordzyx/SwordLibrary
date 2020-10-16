package com.sword.kotlinandroid.lesson

import android.os.Build
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.sword.base.core.BasevView
import com.sword.kotlinandroid.entity.Lesson

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class LessonActivity : AppCompatActivity(), BasevView<LessonPresenter>, Toolbar.OnMenuItemClickListener {
    private val lessonPresenter: LessonPresenter = LessonPresenter(this)

    private val lessonAdapter: LessonAdapter = LessonAdapter()


    fun showResult(lessons: ArrayList<Lesson>) {

    }

    override fun getPresenter(): LessonPresenter {
        return lessonPresenter
    }
}