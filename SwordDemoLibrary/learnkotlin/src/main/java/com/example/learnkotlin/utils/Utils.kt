package com.example.learnkotlin.utils

import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import com.example.learnkotlin.core.BaseApplication

private val displayMetrics = Resources.getSystem().displayMetrics

/**
 * applyDimension 用于将不同尺寸单位的值转换成 px 值，第一个参数是要转换的单位。
 *
 * 顶层函数
 *
 * kotlin 调用：
 *    import com.example.learnkotlin.utils.Utils.dp2px
 *    dp2px(3.0)
 * Java 使用
 *    import com.example.learnkotlin.utils.UtilsKt
 *    UtilsKt.dp2px
 */
fun dp2px(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)

/**
 * kotlin 调用方式 ：
 *    import com.example.learnkotlin.utils.Utils
 *
 *    Utils.toast("....")
 *    Utils.toast("....", Toast.LENGTH_SHORT)
 * Java 调用方式
 *    import com.example.learnkotlin.utils.Utils
 *
 *    Utils.INSTANCE.toast("...")
 *    Utils.INSTANCE.toast("...", Toast.LEAGTH_SHORT)
 */
object Utils {
    /**
     * 只有一行代码的函数可以直接使用 "=" 相连
     *
     * 使用函数参数默认值可以将单参数的函数去掉，直接在 toast(string: String, duration: Int) 中为第二个参数设置默认值。
     *      toast(string: String, duration: Int = Toast.LENGTH_SHORT)
     */
    //fun toast(string: String) = toast(string, Toast.LENGTH_SHORT)

    fun toast(string: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(BaseApplication.currentApplication, string, duration).show()
}