package sword

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import sword.thread.ThreadSample
import java.util.concurrent.Executors


class ClassSample {
  
}

fun main() = main4()


fun main4() {
  /*print(String.format(
    "%s, %,.1f msec elpased",
    "122456121",
    16.891
  ))*/
  
  /*val classSample = ClassSample()
  print("canonicalName: ${classSample.javaClass.canonicalName}, simpleName: ${classSample.javaClass.simpleName}" +
      ", name: ${classSample.javaClass.name}, String ClassName: ${"".javaClass.name}")*/
  
  ThreadSample.threadFactory()
}

/**
 * runBlocking 开启一个阻塞协程
 */
fun main1() = runBlocking {
  flow {
    emit(1)
    emit(2)
    emit(3)
    emit(4)
    emit(5)
  }.filter { it > 2 }
    .map { it * 2 }
    .take(2)
    .collect {
      println(it)
    }
}

fun main2() = runBlocking {
  flowOf(1, 2, 3, 4, 5)
    .filter { it > 2 }
    .map { it * 2 }
    .take(2)
    .collect {
      println(it)
    }
}

fun main3() = runBlocking {
  val mySingleDispatcher = Executors.newSingleThreadExecutor {
    Thread(it, "MySingleThread").apply { isDaemon = true }
  }.asCoroutineDispatcher()
  val scope = CoroutineScope(mySingleDispatcher)
  flow {
    emit(4)
    emit(5)
    emit(6)
    emit(7)
    emit(8)
  }//运行在 DefaultDispatcher
    .flowOn(Dispatchers.IO)
    .filter { //运行在 MySingleThread
      println("Filter: $it")
      it > 2
    }
    .onEach { //运行在 MySingleThread
      println("onEach $it")
    }
    .launchIn(scope)
}