package com.example.swordlibrary.kotlin

import android.os.CountDownTimer
import android.util.Log

object CountDownTimerDemo {
    private val TAG = "CountDownTimerDemo"
    private val countDownTimer = object: CountDownTimer(3000L, 1000L) {
        override fun onFinish() {
            Log.d(TAG, "倒计时完成")
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.d(TAG, "倒计时：${millisUntilFinished/1000}s");
        }
    }

    fun start() {
        countDownTimer.start()
    }
}