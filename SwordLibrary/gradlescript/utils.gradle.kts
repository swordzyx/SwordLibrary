/**
 * 禁止直接在库模块上执行 Release 构建
 * 这是一个安全措施，确保库模块只能作为依赖被引用，而不能直接构建发布
 */
fun disableGradleBuild() {
    gradle.taskGraph.whenReady {
        // 获取 gradlew 命令的执行目录
        println("project.gradle.startParameter.projectDir: ${project.gradle.startParameter.projectDir}")
        project.gradle.startParameter.projectDir?.let {
            if (it.absolutePath.contains(project.name)) {
                throw GradleException("禁止在当前库模块上执行 release 构建")
            }
        }

        if (project.gradle.startParameter.taskNames.any { task ->
            task.contains("${project.path}:") && task.contains("Release", ignoreCase = true)
        }) {
            throw GradleException("禁止在当前库模块上执行 release 构建")
        }
    }
}