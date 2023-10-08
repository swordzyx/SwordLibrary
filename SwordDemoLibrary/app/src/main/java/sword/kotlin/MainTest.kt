package sword.kotlin

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun main() {
  GlobalScope.launch(Dispatchers.IO) { 
    println("Coroutine started: ${Thread.currentThread().name}")
    delay(1000L)
    println("Hello World")
  }
  println("after launch: ${Thread.currentThread().name}")
  Thread.sleep(2000L)
  println("process End")
}


class MainTest {
}