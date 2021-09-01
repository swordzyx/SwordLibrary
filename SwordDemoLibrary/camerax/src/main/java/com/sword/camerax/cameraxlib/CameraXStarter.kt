package com.sword.camerax.cameraxlib

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.sword.camerax.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraXStarter(val activity: AppCompatActivity) {
    private var imageCapture: ImageCapture? = null
    //var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    /**
     * 1. 创建一个异步任务，用于获取 ProcessCameraProvider 对象，通过 ProcessCameraProvider.getInstance()，该方法返回 ListenableFuture，然后为这个 Future 设置一个完成监听器
     * 2. 实例化 Preview 用例，并设置用例使用的 SurfaceProvider，SurfaceProvider 通过 Preview UI 控件获取
     * 3. 通过 bindToLifecycle() 方法将 Preview 用例绑定到 Lifecycle
     */
    fun startCamera(surfaceProvider: Preview.SurfaceProvider) {
        //创建 ProcessCameraProvider 实例，通过此对象将 Camera 的生命周期绑定到 UI 组件的声明周期（Lifecycle）。这样可以不必再使用代码控制相机的打开与关闭。
        //不过 getInstance 返回的是 ListenableFuture 对象，这是一个 Future，Future 表示一个异步的任务。ListenableFuture 接收一个完成监听器，当异步任务完成时，将执行监听器中的代码。
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)
        
        //ContextCompat.getMainExecutor(context) 将返回一个在主线程上执行的 Executor
        //cameraProviderFuture 任务执行完成之后，将在主线程中执行传入的 Runnable
        cameraProviderFuture.addListener({
            //CameraProviderFuture 其实只是给 ProcessCameraProvider 装了一层外壳，此处用于获取 ProcessCameraProvider 实例
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            
            //初始化 Preview 用例，并将 Preview 与 Surface 关联起来，预览页面是通过 Surface 显示的
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(surfaceProvider)
                }
            //初始化 ImageCapture 用例
            imageCapture = ImageCapture.Builder().build()
            
            //设置默认使用的摄像头
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                //在将用例绑定 Lifecycle 之前，解绑所有的旧用例
                cameraProvider.unbindAll()
                //将 preview 用例与 activity 的声明周期关联起来，这样 preview 用例的状态将由 activity 的生命周期托管。
                cameraProvider.bindToLifecycle(activity, cameraSelector, preview)
            } catch (exc: Exception) {
                
            }
        }, ContextCompat.getMainExecutor(activity))
    }
    
    private fun takePhoto(photoFile: File) {
        //拷贝一个 ImageCapture 用例的引用
        val imageCapture = imageCapture ?: return
        
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(activity), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val saveUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeed: $saveUri"
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exception.message}")
            }

        })
    }
    
    fun takePhoto() {
        val photoFile = File(getOutputDirectory(), SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")
        
        takePhoto(photoFile)
    }
    
    private fun getOutputDirectory(): File {
        val mediaDir = activity.externalMediaDirs.firstOrNull()?.let { 
            File(it, activity.resources.getString(R.string.app_name)).apply { 
                mkdirs()
            }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir else activity.filesDir
    }
    
    companion object {
        const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val TAG = "CameraX_Sample"
    }
}