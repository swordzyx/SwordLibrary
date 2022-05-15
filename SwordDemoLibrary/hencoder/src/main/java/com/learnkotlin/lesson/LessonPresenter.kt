package com.example.learnkotlin.lesson

import com.example.learnkotlin.core.EntityCallback
import com.example.learnkotlin.core.HttpClient
import com.example.learnkotlin.utils.Utils.toast
import com.google.gson.reflect.TypeToken

class LessonPresenter(val activity: LessonActivity) {

    //Lesson 类待实现
    private var lessons = emptyList<Lesson>()

    private val type = object : TypeToken<List<Lesson>>(){}.type

    fun fetchData() {
        HttpClient.get(LESSON_PATH, type, object : EntityCallback<List<Lesson>> {
            override fun onSuccess(entity: List<Lesson>) {
                lessons = entity
                activity.runOnUiThread {
                    activity.showResult(lessons)
                }
            }

            override fun onFailure(message: String) {
                activity.runOnUiThread {
                    toast(message)
                }
            }
        })
    }

    fun showPlayback() {
        /* filter 返回的是一个集合，里面传入过滤的条件 */
        activity.showResult(lessons.filter { it.state == Lesson.State.PLAYBACK })
    }

    companion object {
        private const val LESSON_PATH = "lessons"
    }
}