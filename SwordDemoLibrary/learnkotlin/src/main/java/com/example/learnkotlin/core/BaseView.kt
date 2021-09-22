package com.example.learnkotlin.core

interface BaseView<T> {
    fun getPresenter(): T
}