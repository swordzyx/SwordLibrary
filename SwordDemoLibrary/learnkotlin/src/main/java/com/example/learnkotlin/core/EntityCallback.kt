package com.example.learnkotlin.core

interface EntityCallback<T> {
    fun onSuccess(entity: T)
    fun onFailure(message: String)
}