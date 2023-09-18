package com.sword.plugin

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object FileUtilsKt {
    /**
     * 拷贝 source 目录中内容到 target 所指向的目录下
     */
    fun copyDirectory(source: File, target: File) {
        if (!source.exists()) {
            Logger.debug("Source path is not a directory")
            return
        }

        if (target.exists() && !target.isDirectory) {
            Logger.debug("target exists and is not a directory")
            return
        }

        mkdirs(target)
        source.listFiles()?.forEach { file ->
            if (file.isFile) {
                copyFileToDirectory(file, target)
            } else if (file.isDirectory) {
                copyDirectory(file, File(target, file.name))
            } else {
                throw IllegalArgumentException("Illegal file: ${file.absolutePath}")
            }
        }
    }

    fun copyFile(source: File, target: File) {
        Files.copy(source.toPath(), target.toPath(),
            StandardCopyOption.COPY_ATTRIBUTES,
            StandardCopyOption.REPLACE_EXISTING)
    }

    private fun copyFileToDirectory(sourceFile: File, targetDir: File) {
        copyFile(sourceFile, File(targetDir, sourceFile.name))
    }

    private fun mkdirs(folder: File): File {
        if (!folder.mkdirs() && !folder.isDirectory) {
            throw RuntimeException("Cannot create directory $folder");
        }

        return folder
    }
}