package com.sword.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager

class TraceTransform : Transform() {
  override fun getName(): String {
    return "traceTransform"
  }

  override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

  override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

  override fun isIncremental(): Boolean = false

  override fun transform(transformInvocation: TransformInvocation) {
    transformInvocation.inputs.forEach { transformInput -> 
      transformInput.directoryInputs.forEach { directoryInput ->
        traceDirectoryFiles(directoryInput, transformInvocation.outputProvider)
      }
      transformInput.jarInputs.forEach { jarInput ->
        traceJarFiles(jarInput, transformInvocation.outputProvider)
      }
    }
  }
  
  private fun traceDirectoryFiles(directoryInput: DirectoryInput, outputProvider: TransformOutputProvider) {
    directoryInput.file.walkTopDown()
      .filter { it.isFile }
      .forEach {
        ClassReader
      }
  }
  
  private fun traceJarFiles(jarInput: JarInput, outputProvider: TransformOutputProvider) {
    
  }
  
}