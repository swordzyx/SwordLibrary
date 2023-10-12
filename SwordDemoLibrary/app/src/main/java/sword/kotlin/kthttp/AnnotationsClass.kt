package sword.kotlin.kthttp

//修饰函数
@Target(AnnotationTarget.FUNCTION)
//此注解保留到运行时
@Retention(AnnotationRetention.RUNTIME)
annotation class GET(val value: String)

//修饰参数
@Target(AnnotationTarget.VALUE_PARAMETER)
//此注解保留到运行时
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(val value: String)