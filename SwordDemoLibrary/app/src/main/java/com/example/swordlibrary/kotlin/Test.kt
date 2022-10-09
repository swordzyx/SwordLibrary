package com.example.swordlibrary.kotlin

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Test {
  fun main() = runBlocking {
    launch { 
      delay(500)
    }
  }
}