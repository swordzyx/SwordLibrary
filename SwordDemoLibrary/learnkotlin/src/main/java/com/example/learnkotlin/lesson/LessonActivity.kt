package com.example.learnkotlin.lesson

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.learnkotlin.R
import com.example.learnkotlin.core.BaseView

class LessonActivity : AppCompatActivity(), BaseView<LessonPresenter>, Toolbar.OnMenuItemClickListener {

    private val lessonPresenter = LessonPresenter(this@LessonActivity)

    //LessonPresenter 待实现
    override fun getPresenter(): LessonPresenter {
        return lessonPresenter
    }

    private lateinit var refreshLayout : SwipeRefreshLayout
    //LessonAdapter 待实现
    private val lessonAdapter = LessonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        
        val toolbar = findViewById<Toolbar>(R.id.toolbar).apply { 
            inflateMenu(R.menu.menu_lesson)
            setOnMenuItemClickListener(this@LessonActivity)
        }
        
        val recyclerView = findViewById<RecyclerView>(R.id.list).apply { 
            layoutManager = LinearLayoutManager(this@LessonActivity)
            adapter = lessonAdapter
        }
    }


    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }


}