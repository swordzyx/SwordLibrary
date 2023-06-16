package com.sword.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.internal.impldep.com.google.common.base.Preconditions
import org.gradle.internal.impldep.org.apache.ivy.util.FileUtil
import org.gradle.internal.impldep.org.eclipse.jgit.annotations.NonNull
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class SwordTransform extends Transform {
    @Override
    String getName() {
        return "sword"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(@NonNull TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        transformInvocation.inputs.forEach { input ->
            input.jarInputs.forEach { jarInput ->
                def targetFile = transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                println "源文件：${jarInput.file.absolutePath}, 目标文件：${targetFile.absolutePath}"
                copyFile(jarInput.file, targetFile)
            }
            input.directoryInputs.forEach { dirInput ->
                def targetFile = transformInvocation.outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes,
                        Format.DIRECTORY)
                println "源文件：${dirInput.file.absolutePath}, 目标文件：${targetFile.absolutePath}"
                copyDirectory(dirInput.file, targetFile)

            }
        }

    }

    private static void copyDirectory(File source, File target) {
        Preconditions.checkArgument(source.exists(), "Source path is not a directory")
        Preconditions.checkArgument(!target.exists() || target.isDirectory(), "target exists and is not a directory")


    }

    private static void copyFile(File source, File target) {
        Files.copy(source.toPath(), target.toPath(),
                StandardCopyOption.COPY_ATTRIBUTES,
                StandardCopyOption.REPLACE_EXISTING)
    }
}
