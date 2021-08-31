package com.sword.scanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.Image
import android.net.Uri
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.Surface
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.net.URI

class MlKitScanner {
    init {

    }

    fun scanQRCode(image: InputImage) {
        //仅识别二维码
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        //创建 BarcodeScanner 对象
        //val scanner = BarcodeScanning.getClient()
        //创建 BarcodeScanner 对象, 指定配置
        val scannerWithOptions = BarcodeScanning.getClient(options)

        scannerWithOptions.process(image)
            .addOnSuccessListener { barcodes ->
                for(barcode in barcodes) {
                    val bounds = barcode.boundingBox
                    val corners = barcode.cornerPoints
                    val rawValue = barcode.rawValue
                    val valueType = barcode.valueType

                    when(valueType) {
                        Barcode.TYPE_WIFI -> {
                            val ssid = barcode.wifi!!.ssid
                            val password = barcode.wifi!!.password
                            val type = barcode.wifi!!.encryptionType
                        }
                        Barcode.TYPE_URL -> {
                            val title = barcode.url!!.title
                            val url = barcode.url!!.url
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                println("scanner failed: " + exception.message)
                println(exception.cause)
                println("please scan again")
            }
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

    fun createInputImage(context: Context, uri: Uri) {
        val image = InputImage.fromFilePath(context, uri)


    }

    companion object {
        private val ORIENTATIOINS = SparseIntArray()

        init {
            ORIENTATIOINS.append(Surface.ROTATION_0, 0)
            ORIENTATIOINS.append(Surface.ROTATION_90, 90)
            ORIENTATIOINS.append(Surface.ROTATION_180, 180)
            ORIENTATIOINS.append(Surface.ROTATION_270, 270)
        }

        /**
         * 计算 Camera 的旋转角度
         */
        fun calculateRotationDegree(cameraId: String, activity: Activity, isFrontFacing: Boolean): Int {
            val deviceRotation = activity.windowManager.defaultDisplay.rotation
            var rotationCompensation = ORIENTATIOINS.get(deviceRotation)

            //获取相机的方向
            val cameraManager = activity.getSystemService(CAMERA_SERVICE) as CameraManager
            //相机顺时针方向上的旋转角度
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

}