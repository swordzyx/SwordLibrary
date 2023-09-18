package com.sword.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.internal.impldep.org.apache.commons.io.IOUtils
import org.gradle.internal.io.IoUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarInputStream
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

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
        directoryInput: DirectoryInput, outputProvider: TransformOutputProvider
    ) {
        //遍历目录下的文件
        directoryInput.file.walkTopDown().filter { it.isFile }.forEach { file ->
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
        val tempFile = outputProvider.getContentLocation(
            jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR
        )
        Logger.debug("tempFile: ${tempFile.absolutePath}")
        //构造一个 jar 文件对象，用于读取 jar 文件中的内容
        JarFile(jarInput.file).use { jarFile ->
            //获取 jar 文件的输出流，用于写入修改之后的 class
            JarOutputStream(FileOutputStream(tempFile)).use { jarOutputStream ->
                //遍历 jar 文件的每一个条目，获取条目的输入流，将其写入新的 jar 文件中
                jarFile.entries().iterator().forEach { jarEntry ->
                    Logger.debug("遍历到 ${jarEntry.name}")

                    val zipEntry = ZipEntry(jarEntry.name)
                    jarFile.getInputStream(zipEntry).use { jarInputStream ->
                        jarOutputStream.putNextEntry(zipEntry)
                        //如果是 class 文件，就修改 class 文件中的字节码，然后在拷贝到新的 jar 文件中
                        if (jarFile.name.endsWith("class")) {
                            val classReader = ClassReader(IOUtils.toByteArray(jarInputStream))
                            val classWriter = ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                            classReader.accept(
                                TraceClassVisitor(classWriter),
                                ClassReader.EXPAND_FRAMES
                            )
                            jarOutputStream.write(classWriter.toByteArray())
                        } else {
                            jarOutputStream.write(IOUtils.toByteArray(jarInputStream))
                        }
                    }
                }
            }
        }
    }

    /*
    类被访问到时，此类中的方法会被触发
     */
    class TraceClassVisitor(private val classWriter: ClassWriter) :
        ClassVisitor(Opcodes.ASM9, classWriter) {
        private lateinit var className: String

        override fun visit(
            version: Int,
            access: Int,
            name: String,
            signature: String,
            superName: String?,
            interfaces: Array<out String>
        ) {
            super.visit(version, access, name, signature, superName, interfaces)
            className = name
        }

        //这里面返回的 MethodVisitor 实例中的函数会在访问方法时被回调，回到时间包括从开始访问方法到访问结束之间的各个时间段。
        override fun visitMethod(
            access: Int,
            name: String,
            descriptor: String,
            signature: String,
            exceptions: Array<out String>
        ): MethodVisitor {

            Logger.debug("访问方法，name: $name, descriptor: $descriptor, signature: $signature, exception: $exceptions")
            return MethodTraceVisitor(
                className,
                super.visitMethod(access, name, descriptor, signature, exceptions),
                access,
                name,
                descriptor
            )
        }
    }

    /*
     类中的方法被访问到时，此类中的方法会被触发
     */
    class MethodTraceVisitor(
        private val className: String,
        methodVisitor: MethodVisitor,
        access: Int,
        name: String,
        descriptor: String
    ) : AdviceAdapter(Opcodes.ASM9, methodVisitor, access, name, descriptor) {
        //方法开始，需求是插入 Trace.beginSection("类名/方法名")
        override fun onMethodEnter() {
            super.onMethodEnter()
            //1. 将"类名/方法名"压入操作数栈中
            visitLdcInsn("$className/$name")
            //2. 调用 Trace.beginSection 方法
            visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "android/os/Trace",
                "beginSection",
                "(Ljava/lang/String;)V",
                false
            )
        }

        //方法结束，需求是插入 Trace.endSection("类名/方法名")
        override fun onMethodExit(opcode: Int) {
            //调用 Trace.endSection() 方法
            visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "android/os/Trace",
                "endSection",
                "()V",
                false
            )

        }
    }

}