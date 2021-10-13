package com.example.camera2

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.media.ImageReader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Surface
import android.view.SurfaceView
import android.view.WindowManager

class MainActivity : AppCompatActivity() {
    private lateinit var cameraDevice: CameraDevice
    private lateinit var surfaceView: SurfaceView
    private lateinit var imageReader: ImageReader
    private lateinit var displayMetrics: DisplayMetrics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        obtainMetrics()
        
        surfaceView = findViewById(R.id.preview_view)
//        imageReader = ImageReader.newInstance(50.dp, 50.dp, ImageFormat.JPEG, 1)
    }

    

    private fun createCaptureSession(surfaces: List<Surface>) {
        cameraDevice.createCaptureSession(surfaces, object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                TODO("Not yet implemented")
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                TODO("Not yet implemented")
            }

        }, null)
    }

    fun Int.dp(): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), displayMetrics).toInt()

    private fun obtainMetrics() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.getMetrics(displayMetrics)
        } else {
            (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(displayMetrics)
        }
    }
}

