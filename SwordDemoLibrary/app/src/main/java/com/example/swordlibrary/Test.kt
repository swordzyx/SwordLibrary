package com.example.swordlibrary

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking {
  flow {
    emit("a")
    delay(100)
    emit("b")
  }.flatMapLatest { value ->
    flow {
      emit(value)
      delay(200)
      emit(value + "_last")
    }
  }.collect { value ->
    println("value: $value")
  }
  /**
   * 输出结果：当源 flow 生产出 b 之后，为 a 创建的 flow 就被取消掉了，所以 a_last 还没来得及打印。
   * value: a
   * value: b
   * value: b_last
   */
}
