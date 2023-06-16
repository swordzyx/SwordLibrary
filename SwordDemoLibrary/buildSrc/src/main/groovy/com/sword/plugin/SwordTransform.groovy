package com.sword.plugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.Format
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.build.api.transform.QualifiedContent

import org.gradle.internal.impldep.org.apache.ivy.util.FileUtil
import org.gradle.internal.impldep.org.eclipse.jgit.annotations.NonNull
import com.android.utils.FileUtils


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
                FileUtil.copy(jarInput.file, targetFile)
            }
        }

    }
}
