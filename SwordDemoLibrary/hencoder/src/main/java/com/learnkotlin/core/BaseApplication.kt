package com.example.learnkotlin.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context



class BaseApplication : Application() {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        currentApplication = base
    }

    /**
     * 伴生对象，在内部维护一个内部类的单例对象
     */
    companion object {
        /**
         * kotlin 会为 var 类型的成员自动生成 getter 和 setter 方法，因此下面的 currentApplication() 可以省略掉，直接通过 BaseApplciation.currentApplication 的形式来调用
         * 另外，为了防止外部修改 currentApplication 的值，需要将 currentApplication 的 setter 方法设为 private
         *
         * 在 currentApplication 加上 @JvmStatic 注解，将 currentApplication 变为真正的静态成员，在 Java 中通过 BaseApplication.getCurrentApplication() 来获取 currentApplication 。
         *
         * 可以通过 @get: JvmName("currentApplication") 来将 currentApplication 的 get 方法名设置为 "currentApplication"，前面的 @get 表示这个注解作用域 get 方法。之后在 Java 中通过 BaseApplication.currentApplication() 就可以获取 currentApplication 成员。
         *
         * 同样，如果使用 @set:JvmName("setCurrentApplication") 就可以将 currentApplication 的 setter 方法名设置为 setCurrentApplication
         */
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        @get:JvmName("currentApplication")
        var currentApplication: Context? = null
            private set //将 currentApplication 的 setter 设为私有


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
        /*fun currentApplication(): Context? {
            return context
        }*/

        /**
         * 使用 JvmStatic 关键字可以将成员变为真正的静态成员。
         *
         * Java 可以直接通过 BaseApplication.currentApplicationStatic() 格式调用此函数
         *
         * JvmStatic 注解只有在只有在使用了 object 修饰的类中才有用
         */
        /*@JvmStatic
        fun currentApplicationStatic(): Context? {
            return context
        }*/
    }
}