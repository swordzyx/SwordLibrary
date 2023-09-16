package com.sword.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter
import java.io.FileInputStream
import java.util.jar.JarFile

class TraceTransform : Transform() {
  override fun getName(): String {
    return "traceTransform"
  }

  override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> =
    TransformManager.CONTENT_CLASS

  override fun getScopes(): MutableSet<in QualifiedContent.Scope> =
    TransformManager.SCOPE_FULL_PROJECT

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

  private fun traceDirectoryFiles(
    directoryInput: DirectoryInput,
    outputProvider: TransformOutputProvider
  ) {
    //遍历目录下的文件
    directoryInput.file.walkTopDown()
      .filter { it.isFile }
      .forEach { file ->
        FileInputStream(file).use { fis ->
          //读取 class 文件
          val classReader = ClassReader(fis)

          //用于向文件中写字节码
          val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)

          //访问类时回调 classWriterVisitor 中的方法
          val classWriterVisitor = TraceClassVisitor(classWriter)
          //accept 方法将 ClassReader 和 ClassVisitor 链接起来，EXPAND_FRAMES 表示读取完整的 StackMapFrame
          classReader.accept(classWriterVisitor, ClassReader.EXPAND_FRAMES)
        }
      }

    val dest = outputProvider.getContentLocation(
      directoryInput.name,
      directoryInput.contentTypes,
      directoryInput.scopes,
      Format.DIRECTORY
    )
    Logger.debug("拷贝 ${directoryInput.file} 目录下的内容到目标目录 $dest 中")
    //将 class 文件拷贝到目标目录，确保有内容可以继续构建
    FileUtils.copyDirectory(directoryInput.file, dest)
  }

  //jar 文件修改之后要创建一个新的 jar 文件来接收修改之后的内容
  private fun traceJarFiles(jarInput: JarInput, outputProvider: TransformOutputProvider) {
    JarFile(jarInput.file).use { 
      
    }
  }

  /*
  类被访问到时，此类中的方法会被触发
   */
  class TraceClassVisitor(private val classWriter: ClassWriter) :
    ClassVisitor(Opcodes.ASM9, classWriter) {

    //这里面返回的 MethodVisitor 实例中的函数会在访问方法时被回调，回到时间包括从开始访问方法到访问结束之间的各个时间段。
    override fun visitMethod(
      access: Int,
      name: String,
      descriptor: String,
      signature: String,
      exceptions: Array<out String>
    ): MethodVisitor {
      Logger.debug("访问方法，name: $name, descriptor: $descriptor, signature: $signature, exception: $exceptions")
      return MethodTraceVisitor(classWriter, access, name, descriptor)
    }
  }

  /*
   类中的方法被访问到时，此类中的方法会被触发
   */
  class MethodTraceVisitor(
    private val classWriter: ClassWriter,
    access: Int,
    name: String,
    descriptor: String
  ) :
    AdviceAdapter(Opcodes.ASM9, null, access, name, descriptor) {
    //方法开始
    override fun onMethodEnter() {
      super.onMethodEnter()
    }

    //方法结束
    override fun onMethodExit(opcode: Int) {
      super.onMethodExit(opcode)
    }
  }

}