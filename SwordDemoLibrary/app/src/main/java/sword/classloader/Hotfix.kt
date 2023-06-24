package sword.classloader

import android.annotation.SuppressLint
import android.content.Context
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexClassLoader
import okio.*
import java.io.File
import kotlin.io.use
import  java.lang.reflect.Array

class Hotfix {
    fun loadApkToClassLoader(context: Context, apkPath: String) {
        val cacheApkFile = File(context.cacheDir, "hotfix.apk")
        File(apkPath).copyTo(cacheApkFile)

        val targetClassLoader = DexClassLoader(cacheApkFile.path, context.cacheDir.path, null, null)
        replaceDexElements(context.classLoader, targetClassLoader)
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun replaceDexElements(originalClassLoader: ClassLoader, targetClassLoader: ClassLoader) {
        val pathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList").apply {
            isAccessible = true
        }
        //拿到 originalClassLoader 中的 pathList 成员
        val originPathList = pathListField.get(originalClassLoader)

        //拿到 targetClassLoader 中的 pathList 成员
        val targetPathList = pathListField.get(targetClassLoader)
        //拿到 pathList 中的 dexElements 成员
        val dexElementsField = originPathList.javaClass.getDeclaredField("dexElements").apply {
            isAccessible = true
        }
        val targetDexElements = dexElementsField.get(targetPathList)

        //执行 originalClassLoader.pathList.dexElements = targetDexElements
        dexElementsField.set(originPathList, targetDexElements)
    }


    fun addDexToClassLoader(context: Context, dexPath: String) {
        val cacheHotfixDexFile = File(context.cacheDir, "hotfix.dex")
        try {
            File(dexPath).source().buffer().use { source ->
                cacheHotfixDexFile.sink().buffer().use { sink ->
                    sink.writeAll(source)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        addDexClassToClassLoader(DexClassLoader(cacheHotfixDexFile.path, context.cacheDir.path, null, null), context.classLoader)
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun addDexClassToClassLoader(hotfixClassLoader: ClassLoader, ClassLoader: ClassLoader) {
        val pathListField = BaseDexClassLoader::class.java.getDeclaredField("pathList").apply {
            isAccessible = true
        }

        //originalClassLoader.pathList
        val originalPathList = pathListField.get(ClassLoader)
        //拿到 targetPathList 的 dexElements 成员
        val dexElementsField = originalPathList.javaClass.getDeclaredField("dexElements").apply {
            isAccessible = true
        }
        //originalClassLoader.pathList.dexElements
        val originalDexElements = dexElementsField.get(originalPathList)

        //hotfixClassLoader.pathList.dexElements
        val hotfixDexElements = dexElementsField.get(pathListField.get(hotfixClassLoader))

        if (originalDexElements == null || hotfixDexElements == null) {
            return
        }
        val hotfixDexElementsLength = Array.getLength(hotfixDexElements)
        val originDexElementsLength = Array.getLength(originalDexElements)
        val finalDexElements = Array.newInstance(originalDexElements.javaClass.componentType!!,
            hotfixDexElementsLength + originDexElementsLength)

        for (i in 0 until hotfixDexElementsLength) {
            Array.set(finalDexElements, i, Array.get(hotfixDexElements, i))
        }
        for (i in 0 until originDexElementsLength) {
            Array.set(finalDexElements, hotfixDexElementsLength + i, Array.get(originalDexElements, i))
        }

        dexElementsField.set(originalPathList, finalDexElements)
    }
}