package com.sword.customviewset.view

import android.animation.TypeEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

private val provinces = listOf("北京市",
        "天津市",
        "上海市",
        "重庆市",
        "河北省",
        "山西省",
        "辽宁省",
        "吉林省",
        "黑龙江省",
        "江苏省",
        "浙江省",
        "安徽省",
        "福建省",
        "江西省",
        "山东省",
        "河南省",
        "湖北省",
        "湖南省",
        "广东省",
        "海南省",
        "四川省",
        "贵州省",
        "云南省",
        "陕西省",
        "甘肃省",
        "青海省",
        "台湾省",
        "内蒙古自治区",
        "广西壮族自治区",
        "西藏自治区",
        "宁夏回族自治区",
        "新疆维吾尔自治区",
        "香港特别行政区",
        "澳门特别行政区")

class ProvinceView(context: Context, attrs: AttributeSet): View(context, attrs) {


    var province = "天津市"
        set(value) {
            field = value
            invalidate()
        }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 16.dp
        textAlign = Paint.Align.CENTER
    }
    private val rect = Rect()

    override fun onDraw(canvas: Canvas) {
        paint.getTextBounds(province, 0, province.length, rect)
        canvas.drawText(province, (width/2).toFloat(), (height/2).toFloat(), paint)
    }
}
class ProvinceEvaluator: TypeEvaluator<String> {
    override fun evaluate(fraction: Float, startValue: String, endValue: String): String {
        val start = provinces.indexOf(startValue)
        val end = provinces.indexOf(endValue)
        val cur = (end - start) * fraction
        return provinces[cur.toInt()]
    }

}
