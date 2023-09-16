
package com.sword.plugin

class Logger {
  companion object {
    private val tag = "SwordGradlePlugin"
    
    @JvmStatic
    fun debug(message: String) {
      println("$tag : $message")
    }
  }
}
