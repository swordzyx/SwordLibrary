package com.sword.customviewset

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import com.sword.customviewset.drawable.MeshDrawable

class DrawableAndBitmapActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable_and_bitmap)

        val bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888)
        //原理：构造一个 BitmapDrawable，使用 Canvas 将 Bitmap 绘制到 Drawable 中，Drawable 其实跟 View 是类似的，它是一套绘制规则。然后 canvas 根据这个规则绘制内容。
        bitmap.toDrawable(resources)

        val drawable = ColorDrawable()
        //如果是 BitmapDrawable 类型，直接返回保存的 Bitmap 即可
        //如果不是，创建一个只包含 Bitmap 的 Canvas，然后调用 draw 方法，将 Drawable 的内容重新绘制到这个 Canvas 中，这就相当于将像素信息填充到了 Bitmap 中。接着返回 Bitmap 即可。
        drawable.toBitmap()
    }
}