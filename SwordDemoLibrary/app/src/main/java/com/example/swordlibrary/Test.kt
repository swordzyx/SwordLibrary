package com.example.swordlibrary

import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class Test {
  fun main() = runBlocking {
    /*launch { 
      delay(500)
    }*/
    
    //testCoroutine()
    
    /*suspendCoroutine<String> { continuation ->
      continuation.resume("hello")
    }*/
    
    val scope = CoroutineScope(Job())
    scope.launch { 
      
      delay(1000)
    }
    
  }

  /*suspend fun noSuspendTestCoroutine() {
    println("start")
    val user = getUserInfo()
    println(user)
    val friendList = noSuspendFriendList(user)
    println(friendList)
    val feedList = getFeedList(user, friendList)
    println(feedList)
  }
  
  suspend fun testCoroutine(): String {
    println("start")
    val user = getUserInfo()
    println(user)
    val friendList = getFriendList(user)
    println(friendList)
    val feedList = getFeedList(user, friendList)
    println(feedList)
    return feedList
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
  }*/
}