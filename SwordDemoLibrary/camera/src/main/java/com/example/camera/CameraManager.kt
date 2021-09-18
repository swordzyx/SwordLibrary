package com.example.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import java.lang.Exception

class CameraManager {

    /**
     * 获取 Camera 实例
     */
    fun getCameraInstance(cameraId: Int): Camera? {
        val cameraNum = Camera.getNumberOfCameras()
        var resultId = cameraId
        
        if (cameraId < cameraNum) {
            var id = 0
            while (id < cameraNum) {
                val info = Camera.CameraInfo()
                Camera.getCameraInfo(id, info)
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    resultId = id
                    break
                }
                ++id
            }
            if (id == cameraNum) {
                resultId = 0
            }
        }
        
        return try {
            Camera.open(resultId)
        } catch (e: Exception) {
            null
        }
    }

    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }
    
    
}