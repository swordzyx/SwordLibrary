package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
/**
 * 为 LiveData 设置一个测试的 Observer，默认 2s 之内如果数据发生改变，则将新的数据返回。如果过了指定时间数据依然没有发生改变，则抛出一个 TimeoutException 异常。
 */
@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserver: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            data = t
            //数据发生改变之后，调用 countDown()，await() 就会返回 true。
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    this.observeForever(observer)

    try {
        afterObserver.invoke()

        //使当前线程休眠，直到 latch 倒计时结束，当倒计时结束时，await 方法会立刻返回 true
        //使当前线程休眠 2s ，如果 2s 之内 latch.count 变为了 0，则 await() 返回 true。若 2s 倒计时结束，latch.count 大于 0，则 await 返回 false
        //每调用一次 countDown()，latch.count 就会减一
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        this.removeObserver(observer)
    }
    return data as T
}