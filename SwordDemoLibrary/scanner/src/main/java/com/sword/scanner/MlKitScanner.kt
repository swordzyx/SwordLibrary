package com.sword.scanner

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.common.InputImage

class MlKitScanner {

    fun scanQRCode() {
        //仅识别二维码
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
    }

    //CameraX 里面的 analysis 用例，通过此用例可直接访问 Camera 的缓冲数据，并在这些数据上执行自定义的操作（比如进行机器学习）
    private class CameraImageAnalyser : ImageAnalysis.Analyzer {
        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy : ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            }
        }
    }

}