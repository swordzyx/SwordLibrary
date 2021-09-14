package com.example.learnkotlin.main

import android.content.Context
import android.hardware.Camera
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class CameraPreview(context: Context, private val mCamera: Camera) : SurfaceView(context), SurfaceHolder.Callback {
    private val mHolder: SurfaceHolder = holder.apply { 
        addCallback(this@CameraPreview)
    }
    
    
    override fun surfaceCreated(holder: SurfaceHolder) {
        mCamera.apply { 
            try {
                
            } catch (e: IOException) {
                LogUtil.debug("")
            }
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        TODO("Not yet implemented")
    }

}