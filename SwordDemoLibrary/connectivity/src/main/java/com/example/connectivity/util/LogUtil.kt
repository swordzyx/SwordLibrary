@file:JvmName("LogUtilKt")
package com.example.connectivity.util
import android.util.Log


class LogUtil {
    object INSTANCE {
        private val TAG = "connectivity"

        fun debug(str: String) {
            Log.d(TAG, str)
        }
    }


}
