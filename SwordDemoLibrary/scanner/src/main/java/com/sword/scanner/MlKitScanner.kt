package com.sword.scanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.Surface
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

    private val ORIENTATIOINS = SparseIntArray()

    init {
        ORIENTATIOINS.append(Surface.ROTATION_0, 0)
        ORIENTATIOINS.append(Surface.ROTATION_90, 90)
        ORIENTATIOINS.append(Surface.ROTATION_180, 180)
        ORIENTATIOINS.append(Surface.ROTATION_270, 270)
    }

    fun calculateRotationDegree(cameraId: String, activity: Activity, isFrontFacing: Boolean): Int {
        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = ORIENTATIOINS.get(deviceRotation)

        //获取相机的选装方向
        val cameraManager = activity.getSystemService(CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        //前置摄像头
        if (isFrontFacing) {
            rotationCompensation = (sensorOrientation + rotationCompensation) % 360
        } else { //后置摄像头
            rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360
        }

        return rotationCompensation
    }

}