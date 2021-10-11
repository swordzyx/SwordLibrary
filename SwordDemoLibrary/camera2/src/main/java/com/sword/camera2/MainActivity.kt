package com.sword.camera2

import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.media.ImageReader
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Surface
import android.view.SurfaceView

class MainActivity : AppCompatActivity() {
    lateinit var cameraDevice : CameraDevice
    lateinit var surfaceView: SurfaceView
    lateinit var imageReader: ImageReader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surfaceView = findViewById(R.id.preview_view)
        imageReader = ImageReader.newInstance()

    }

    private fun createCaptureSession(surfaceList: List<Surface>) {
        cameraDevice.createCaptureSession(surfaceList, object : CameraCaptureSession.StateCallback() {
            override fun onConfigured(session: CameraCaptureSession) {
                TODO("Not yet implemented")
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                TODO("Not yet implemented")
            }

        }, null)
    }
}