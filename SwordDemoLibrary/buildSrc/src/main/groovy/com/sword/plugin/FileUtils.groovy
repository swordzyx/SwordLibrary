package com.sword.plugin

import java.nio.file.Files
import java.nio.file.StandardCopyOption;

class FileUtils {
    static void copyDirectory(File source, File target) {
        if (!source.exists()) {
            println "Source path is not a directory"
            return
        }

        if (target.exists() && !target.isDirectory()) {
            println "target exists and is not a directory"
            return
        }

        mkdirs(target)
        File[] children = source.listFiles()
        for (File file : children) {
            if (file.isFile()) {
                copyFileToDirectory(file, target)
            } else if (file.isDirectory()) {
                copyDirectory(file, new File(target, file.getName()))
            } else {
                throw new IllegalArgumentException("Illegal file: ${file.getAbsolutePath()}")
            }
        }
    }

    static void copyFile(File source, File target) {
        Files.copy(source.toPath(), target.toPath(),
                StandardCopyOption.COPY_ATTRIBUTES,
                StandardCopyOption.REPLACE_EXISTING)
    }

    private static void copyFileToDirectory(File sourceFile, File targetDir) {
        copyFile(sourceFile, new File(targetDir, sourceFile.getName()))
    }

    static File mkdirs(File folder) {
        if (!folder.mkdirs() && !folder.isDirectory()) {
            throw new RuntimeException("Cannot create directory " + folder);
        }

        return folder
    }
}
