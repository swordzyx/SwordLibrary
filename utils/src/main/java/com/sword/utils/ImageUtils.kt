package com.sword.utils

import android.media.Image
import com.sword.logger.Logger

fun yuv420888ToNv21(image: Image): ByteArray? {
    // 获取图像的宽度和高度
    val width = image.width
    val height = image.height

    // 计算 Y 平面和 UV 平面的大小
    val ySize = width * height
    val uvSize = width * height / 4

    // 创建一个字节数组来存储 NV21 格式的数据
    val nv21 = ByteArray(ySize + uvSize * 2)

    // 获取 Y、U、V 平面的缓冲区
    val yBuffer = image.planes[0].buffer
    val uBuffer = image.planes[1].buffer
    val vBuffer = image.planes[2].buffer

    // 检查 Y 平面的像素步幅是否为 1
    if (image.planes[0].pixelStride != 1) {
        Logger.e("yuv420888ToNv21", "error pixelStride")
        return null
    }

    // 获取 Y 平面的行步幅
    var rowStride = image.planes[0].rowStride
    // 初始化位置指针
    var pos = 0

    // 如果 Y 平面的行步幅等于图像宽度，直接复制 Y 数据
    if (rowStride == width) {
        yBuffer.get(nv21, 0, ySize)
        pos += ySize
    } else {
        // 否则，逐行复制 Y 数据
        var yBufferPos = -rowStride
        while (pos < ySize) {
            yBufferPos += rowStride
            yBuffer.position(yBufferPos)
            //Y 平面的步幅通常为 1 ，因此可以直接复制整行数据
            yBuffer.get(nv21, pos, width)
            pos += width
        }
    }

    // 获取 V 平面的行步幅和像素步幅
    rowStride = image.planes[2].rowStride
    val pixelStride = image.planes[2].pixelStride

    // 检查 U 和 V 平面的行步幅和像素步幅是否一致
    if (rowStride != image.planes[1].rowStride || pixelStride != image.planes[1].pixelStride) {
        Logger.e("yuv420888ToNv21", "pixel stride of u and v plane are different")
        return null
    }

    // 如果像素步幅为 2 且行步幅等于宽度，直接复制 UV 数据
    if (pixelStride == 2 && rowStride == width && uBuffer[0] == vBuffer[1]) {
        vBuffer.get(nv21, ySize, uvSize)
        uBuffer.get(nv21, ySize + 1, uvSize)
    } else {
        // 否则，逐行复制 UV 数据
        for (row in 0 until height / 2) {
            val vuRowStart = row * rowStride
            for (col in 0 until width / 2) {
                val vuPos = vuRowStart + col * pixelStride
                nv21[ySize + row * width + col * 2] = vBuffer[vuPos]
                nv21[ySize + row * width + col * 2 + 1] = uBuffer[vuPos]
            }
        }
    }

    // 返回 NV21 格式的字节数组
    return nv21
}