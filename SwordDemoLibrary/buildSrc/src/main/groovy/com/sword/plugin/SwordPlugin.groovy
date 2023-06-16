package com.sword.plugin

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SwordPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        //其实就相当与创建一个 SwordExtension 类的实例
        def extension = project.extensions.create("sword", SwordExtension)

        project.afterEvaluate {
            println "apply ${extension.name}"
        }
    }
}
