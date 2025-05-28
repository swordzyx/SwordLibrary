package com.swordlibrary.exception

import com.sword.logger.Logger;

object ExceptionHandler {
    private const val tag = "ExceptionHandler"

    private var originalExceptionHandler: Thread.UncaughtExceptionHandler? = null

    internal fun init() {
        originalExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            //全局捕获到异常
            Logger.e(tag, "catch exception: ${throwable.localizedMessage} on thread: ${thread.name}")
            originalExceptionHandler?.uncaughtException(thread, throwable)
            handleException(throwable)
            //todo：打印和上报异常
        }
    }

    private fun handleException(throwable: Throwable) {
        //上报异常
        //如果上报失败则保存到错误日志中
        Logger.e("catch excepiton on global handler", throwable);
    }
}