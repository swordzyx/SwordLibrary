package com.sword.base.core

interface EntityCallback<T> {
    fun onSuccess(entity: T)

    fun onFailure(messge: String?)
}