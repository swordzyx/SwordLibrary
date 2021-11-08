package com.example.learnkotlin.lesson

import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.learnkotlin.R
import com.example.learnkotlin.core.BaseView
import com.example.learnkotlin.utils.CacheUtilsObject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LessonActivity : AppCompatActivity(), BaseView<LessonPresenter>, Toolbar.OnMenuItemClickListener {

    /**
     * 将 token 的赋值和获取都委托给了 Saver 对象
     */
    var token: String by Saver("token")

    class Saver(private val key: String) : ReadWriteProperty<LessonActivity, String> {
        override fun getValue(thisRef: LessonActivity, property: KProperty<*>): String {
            return CacheUtilsObject.get(key)!!
        }

        override fun setValue(thisRef: LessonActivity, property: KProperty<*>, value: String) {
            CacheUtilsObject.save(key, value)
        }
    }


    /**
     * Java 则是使用 LessonActivity.this 来获取外部类引用2 
     */
    //使用了委托，就无需在使用一个成员变量来保存 LessonPresenter 对象了。
    //private val lessonPresenter = LessonPresenter(this@LessonActivity)

    /**
     * by lazy 有两个效果，1. by lazy 里面的代码只会加载一次，也就是 LessonPresenter 对象只会被创建一次。且只会在访问到 presenter 这个成员的时候才会被创建出来
     *
     * 核心实现都在 by 关键字上，kotlin 中通过 by 关键字将统一的操作委托给统一的对象
     */
    override val presenter: LessonPresenter by lazy {
        LessonPresenter(this@LessonActivity)
    }

    /*override fun getPresenter(): LessonPresenter {
        return lessonPresenter
    }*/



    private lateinit var refreshLayout : SwipeRefreshLayout
    private val lessonAdapter = LessonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson)
        
        findViewById<Toolbar>(R.id.toolbar).run {
            inflateMenu(R.menu.menu_lesson)
            setOnMenuItemClickListener(this@LessonActivity)
        }
        
        findViewById<RecyclerView>(R.id.list).run {
            layoutManager = LinearLayoutManager(this@LessonActivity)
            adapter = lessonAdapter
            addItemDecoration(DividerItemDecoration(this@LessonActivity, LinearLayout.VERTICAL))
        }

        refreshLayout = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_layout).apply {
            setOnRefreshListener {
                presenter.fetchData()
            }
            isRefreshing = true
        }


        presenter.fetchData()
    }

    fun showResult(lessons: List<Lesson>) {
        lessonAdapter.updateAndNotify(lessons)
        refreshLayout.isRefreshing = false
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        presenter.showPlayback()
        return false
    }
    
}