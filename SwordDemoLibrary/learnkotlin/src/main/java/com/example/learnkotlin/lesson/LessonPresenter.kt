package com.example.learnkotlin.lesson

import com.google.gson.reflect.TypeToken

class LessonPresenter(val activity: LessonActivity) {
    private val LESSON_PATH = "lessons"

    //Lesson 类待实现
    private val lessons = emptyList<Lesson>()

    private val type = object : TypeToken<List<Lesson>>(){}.type

    fun fetchData() {

    }
}