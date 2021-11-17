package com.example.camera

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.utilclass.LogCollector
import java.io.IOException

class CameraPreview(context: Context, private val mCamera: Camera) : SurfaceView(context), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder = holder.apply { 
        addCallback(this@CameraPreview)
    }
    
    
    override fun surfaceCreated(holder: SurfaceHolder) {
        LogCollector.debug("surfaceCreated")
        mCamera.apply { 
            try {
                setPreviewDisplay(holder)
                startPreview()
            } catch (e: IOException) {
                LogCollector.debug("Error setting camera preview: ${e.message}")
            }
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        LogCollector.debug("surfaceChanged")
        if(mHolder.surface == null) {
            return
        }
        
        try {
            mCamera.stopPreview()
        } catch (e: Exception) {
            
        }
        
        mCamera.apply {
            try {
                setPreviewDisplay(mHolder)
                startPreview()
            } catch (e: Exception) {
                LogCollector.debug("Error starting camera preview: ${e.message}")
            }
            
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        LogCollector.debug("surfaceDestroyed")
    }

}