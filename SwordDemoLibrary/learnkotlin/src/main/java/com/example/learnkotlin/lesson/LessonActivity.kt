package com.example.learnkotlin.lesson

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class LessonActivity : AppCompatActivity(), BaseView<LessonPresenter>, Toolbar.OnMenuItemClickListener {

    private val lessonPresenter = LessonPresenter(this@LessonActivity)

    fun LessonPresenter getPresenter(): LessonPresenter {
        return lessonPresenter
    }

    private val lessonAdapter = LessonAdapter()
    private var refreshLayout : SwipeRefreshLayout?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }


}