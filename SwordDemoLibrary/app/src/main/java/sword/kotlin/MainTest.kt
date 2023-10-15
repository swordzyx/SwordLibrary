package sword.kotlin

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import kotlin.random.Random
import kotlin.system.measureTimeMillis




fun main() {
  flowSample()
}


fun flowSample() = runBlocking {
  flow {
    emit(1)
    emit(2)
    emit(3)
    emit(4)
    emit(5)
  }.filter { it > 2 }
    .map {
      it * 2
    }
    .take(2)
    .collect {
      logx("collect: $it")
    }

  flowOf(1, 2, 3, 4, 5).filter { it > 2 }
    .map {
      it * 2
    }
    .take(2)
    .collect {
      logx("collect: $it")
    }
}

fun channelSample() = runBlocking {
  val channel = Channel<Int>(
    capacity = Channel.CONFLATED,
    onUndeliveredElement = {
      logx("onUndeliveredElement: $it")
    })

  val channel1 = produce {
    (1..3).forEach {
      send(it)
      logx("Send: $it")
    }
  }

  channel1.consumeEach {
    logx("Receive: $it")
  }

  logx("end")
}

val mySingleDispatcher = Executors.newSingleThreadExecutor {
  Thread(it, "MySingleThread").apply { isDaemon = true }
}.asCoroutineDispatcher()
@OptIn(ExperimentalStdlibApi::class)
fun coroutineContextSample() = runBlocking(mySingleDispatcher) {
  /*val user = getUserInfo(this)
  logx(user)*/

  val scope = CoroutineScope(Job() + mySingleDispatcher)
  scope.launch(CoroutineName("FirstCoroutine")) {
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

suspend fun coroutineByAsync(coroutineScope: CoroutineScope) {
  println("In runBlocking: ${Thread.currentThread().name}")

  val deferred: Deferred<String> = coroutineScope.async {
    println("In async: ${Thread.currentThread().name}")
    delay(1000)
    println("after delay")
    return@async "Async Task completed"
  }
  println("After async: ${Thread.currentThread().name}")
  //println("async result: ${deferred.await()}")
  delay(2000)
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
  println("$any Thread: ${Thread.currentThread().name}")
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

fun coroutineByLaunch () {
  GlobalScope.launch(Dispatchers.IO) {
    println("Coroutine started: ${Thread.currentThread().name}")
  }
}
