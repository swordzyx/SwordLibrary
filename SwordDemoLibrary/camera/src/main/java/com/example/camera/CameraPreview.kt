package com.example.camera

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.utilclass.LogUtil
import java.io.IOException

class CameraPreview(context: Context, private val mCamera: Camera) : SurfaceView(context), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder = holder.apply { 
        addCallback(this@CameraPreview)
    }
    
    
    override fun surfaceCreated(holder: SurfaceHolder) {
        LogUtil.debug("surfaceCreated")
        mCamera.apply { 
            try {
                setPreviewDisplay(holder)
                startPreview()
            } catch (e: IOException) {
                LogUtil.debug("Error setting camera preview: ${e.message}")
            }
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        LogUtil.debug("surfaceChanged")
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
                LogUtil.debug("Error starting camera preview: ${e.message}")
            }
            
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        LogUtil.debug("surfaceDestroyed")
    }

}