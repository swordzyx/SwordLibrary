package com.example.swordlibrary.net.retrofit

sealed class ResultX<out R: Any> {
  data class Success<out T: Any>(val data: T): ResultX<T>()
  data class Error(val exception: Exception): ResultX<Exception>()
  
  object Loading: ResultX<Nothing>()
}
