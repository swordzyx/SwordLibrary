package com.sword.kotlinandroid.lesson

import android.app.Activity
import com.google.gson.reflect.TypeToken
import com.sword.base.core.EntityCallback
import com.sword.base.core.HttpClient
import com.sword.base.utils.toast
import com.sword.kotlinandroid.entity.Lesson
import java.lang.reflect.Type

class LessonPresenter {
    private val LESSION_PATH = "lessons"

    private lateinit var activity: Activity

    constructor(activity: Activity) {
        this.activity = activity
    }

    private var lessons: ArrayList<Lesson> = ArrayList()
    private val type: Type = object: TypeToken<List<Lesson>>() {}.type

    fun fetchData() {
        HttpClient.get(LESSION_PATH, type, object : EntityCallback<List<Lesson>> {
            override fun onFailure(messge: String?) {
                activity.runOnUiThread {
                    toast(messge)
                }
            }

            override fun onSuccess(entity: List<Lesson>) {

            }
        })

    }


}