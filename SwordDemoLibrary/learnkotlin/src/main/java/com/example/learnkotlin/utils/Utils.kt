package com.example.learnkotlin.utils

import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import com.example.learnkotlin.core.BaseApplication
import java.lang.reflect.TypeVariable

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
// */
//fun dp2px(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)


/**
 * 为 Float 这个类定义一个扩展函数。
 * 使用示例：4f.dp2px()  将返回 4dp 对应的 px 值
 */
fun Float.dp2px(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, displayMetrics)

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
     *
     * 不过 kotlin 中有函数参数默认值的函数不会暴露多个重载的方法给 Java ，因此在 Java 中调用 Utils.INSTANCE.toast("") 时会报错。
     * 加上 @JvmOverloads，这样就会生成对应的重载函数，Java 中再执行 Utils.INSTANCE.toast("") 就不会再报错了
     */
    //fun toast(string: String) = toast(string, Toast.LENGTH_SHORT)

    @JvmOverloads
    fun toast(string: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(BaseApplication.currentApplication, string, duration).show()
}