package com.example.learnkotlin.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context



class BaseApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        context = base
    }

    /**
     * 伴生对象，在内部维护一个内部类的单例对象
     */
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        /**
         * Java 调用
         *    import com.example.learnkotlin.core.BaseApplication
         *
         *    BaseApplication.Companion.currentApplication()
         *
         * Kotlin 调用
         *    import com.example.learnkotlin.core.BaseApplication
         *
         *    BaseApplication.currentApplication()
         */
        fun currentApplication(): Context? {
            return context
        }

        /**
         * 使用 JvmStatic 关键字可以将成员变为真正的静态成员。
         *
         * Java 可以直接通过 BaseApplication.currentApplicationStatic() 格式调用此函数
         *
         * JvmStatic 注解只有在只有在使用了 object 修饰的类中才有用
         */
        @JvmStatic
        fun currentApplicationStatic(): Context? {
            return context
        }
    }
}