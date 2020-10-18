package com.sword.kotlinandroid.lesson

import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sword.base.core.BasevView
import com.sword.kotlinandroid.R
import com.sword.kotlinandroid.entity.Lesson

class LessonActivity() : AppCompatActivity(), BasevView<LessonPresenter>, Toolbar.OnMenuItemClickListener {
    private val lessonAdapter: LessonAdapter = LessonAdapter()

    override val presenter: LessonPresenter by lazy {
        LessonPresenter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("SWORD", "LessonActivity----onCreate")
        setContentView(R.layout.activity_lesson)

        findViewById<Toolbar>(R.id.toolbar).run{
            inflateMenu(R.menu.menu_lesson)
            setOnMenuItemClickListener(this@LessonActivity)
        }


        findViewById<RecyclerView>(R.id.list).run {
            layoutManager = LinearLayoutManager(this@LessonActivity)
            adapter = lessonAdapter
            addItemDecoration(DividerItemDecoration(this@LessonActivity, LinearLayout.VERTICAL))
        }

        findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout).run {
            setOnRefreshListener { presenter.fetchData() }
            isRefreshing = true
        }

        presenter.fetchData()
    }

    fun showResult(lessons: List<Lesson>) {
        lessonAdapter.updateAndNotify(lessons)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        presenter.showPlayback()
        return false
    }
}