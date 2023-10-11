package sword.kotlin

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.random.Random
import kotlin.system.measureTimeMillis


val mySingleDispatcher = Executors.newSingleThreadExecutor {
  Thread(it, "MySingleThread").apply { isDaemon = true }
}.asCoroutineDispatcher()

@OptIn(ExperimentalStdlibApi::class)
fun main() = runBlocking(mySingleDispatcher) {
  /*val user = getUserInfo(this)
  logx(user)*/
  
  val scope = CoroutineScope(Job() + mySingleDispatcher)
  scope.launch { 
    logx(coroutineContext[CoroutineDispatcher] == mySingleDispatcher)
    delay(1000)
    logx("First end")
  }
  
  delay(500L)
  scope.cancel()
  delay(1000L)
}

suspend fun getUserInfo(coroutineScope: CoroutineScope): String {
  logx("Before IO Context")
  val result = withContext(Dispatchers.IO) {
    logx("In IO Context")
    delay(1000L)
    logx("after delay")
  }
  logx("After IO Context, Context result: $result")
  return "BoyCoder"
}

fun Job.log() {
  logx(
    """
  isActive=$isActive
  isCancelled=$isCancelled
  isCompleted=$isCompleted
  """.trimIndent()
  )
}

fun logx(any: Any?) {
  println(
    """
==========================
$any
Thread: ${Thread.currentThread().name}
==========================""".trimIndent()
  )
}

private suspend fun joinApiSample(coroutineScope: CoroutineScope) {
  suspend fun download() {
    val time = (Random.nextDouble() * 1000).toLong()
    logx("Delay time: $time")
    delay(time)
  }

  val job = coroutineScope.launch(start = CoroutineStart.LAZY) {
    logx("Coroutine start")
    download()
    logx("Coroutine End")
  }
  delay(500L)
  job.log()
  job.start()
  job.log()
  job.invokeOnCompletion {
    job.log()
  }
  job.join()
  logx("Process end")
}

private suspend fun coroutineAwaitSample(coroutineScope: CoroutineScope) {
  suspend fun getResult1(): String {
    delay(1000L)
    return "Result1"
  }

  suspend fun getResult2(): String {
    delay(1000L)
    return "Result2"
  }

  suspend fun getResult3(): String {
    delay(1000L)
    return "Result3"
  }

  var results: List<String>
  val time = measureTimeMillis {

    val result1 = coroutineScope.async { getResult1() }
    val result2 = coroutineScope.async { getResult2() }
    val result3 = coroutineScope.async { getResult3() }

    results = listOf(result1.await(), result2.await(), result3.await())
  }
  println("Time: $time")
  println("results: $results")
}