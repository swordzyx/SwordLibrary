package com.example.swordlibrary.kotlin

import android.util.Log
import kotlinx.coroutines.*

class Test {
  fun main() = runBlocking {
    /*launch { 
      delay(500)
    }*/
    
    testCoroutine()
  }

  suspend fun noSuspendTestCoroutine() {
    println("start")
    val user = getUserInfo()
    println(user)
    val friendList = noSuspendFriendList(user)
    println(friendList)
    val feedList = getFeedList(user, friendList)
    println(feedList)
  }
  
  suspend fun testCoroutine() {
    println("start")
    val user = getUserInfo()
    println(user)
    val friendList = getFriendList(user)
    println(friendList)
    val feedList = getFeedList(user, friendList)
    println(feedList)
  }
  
  suspend fun noSuspendFriendList(user: String): String {
    return "Tom, Jack"
  }

  suspend fun getUserInfo(): String {
    withContext(Dispatchers.IO) {
      delay(1000L)
    }
    return "BoyCoder"
  }

  suspend fun getFriendList(user: String): String {
    withContext(Dispatchers.IO) {
      delay(1000L)
    }
    return "Tom, Jack"
  }

  suspend fun getFeedList(user: String, list: String): String {
    withContext(Dispatchers.IO) {
      delay(1000L)
    }
    return "{FeedList..}"
  }
}