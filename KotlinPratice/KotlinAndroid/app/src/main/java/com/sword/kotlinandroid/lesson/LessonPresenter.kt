package com.sword.kotlinandroid.lesson

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.sword.base.core.EntityCallback
import com.sword.base.core.HttpClient
import com.sword.base.utils.toast
import com.sword.kotlinandroid.entity.Lesson
import java.lang.reflect.Type

class LessonPresenter {
    private val LESSION_PATH = "lessons"

    private var activity: LessonActivity

    constructor(activity: LessonActivity) {
        this.activity = activity
    }

    private var lessons: ArrayList<Lesson> = ArrayList()
    private val type: Type = object: TypeToken<List<Lesson>>() {}.type

    fun fetchData() {
        Log.d("SWROD", "fetchData")
        HttpClient.get(LESSION_PATH, type, object : EntityCallback<List<Lesson>> {
            override fun onFailure(messge: String?) {
                activity.runOnUiThread {
                    toast(messge)
                }
            }

            override fun onSuccess(entity: List<Lesson>) {
                //通过 this@类名 获取该类名的引用，这里的 this@LessonPresenter 就是获取 LessonPresenter 的引用
                this@LessonPresenter.lessons = entity as ArrayList<Lesson>
                activity.runOnUiThread {
                    activity.showResult(lessons)
                }
            }
        })

    }

    fun showPlayback() {
        val playbackLessons: ArrayList<Lesson> = ArrayList()
        for (lesson in lessons) {
            if (lesson.state == Lesson.State.PLAYBACK) {
                playbackLessons.add(lesson)
            }
        }
    }


}