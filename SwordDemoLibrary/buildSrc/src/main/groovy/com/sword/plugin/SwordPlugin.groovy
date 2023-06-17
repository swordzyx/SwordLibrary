package com.sword.plugin

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class SwordPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        //其实就相当与创建一个 SwordExtension 类的实例
        def extension = project.extensions.create("sword", SwordExtension)

        project.afterEvaluate {
            println "apply ${extension.name}"
        }

        def transform = new SwordTransform()
        def baseExtension = project.extensions.getByType(BaseExtension)
        baseExtension.registerTransform(transform)

    }
}
