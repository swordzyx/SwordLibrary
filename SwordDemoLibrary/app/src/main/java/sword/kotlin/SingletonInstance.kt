package sword.kotlin

class SingletonInstance private constructor(val user: String) {
    companion object {
        @Volatile
        private var instance: SingletonInstance? = null

        fun getInstance(name: String): SingletonInstance = instance ?: synchronized(this) {
            instance ?: SingletonInstance(name).also {
                instance = it
            }
        }
    }
}

/**
 * double check 单例模式接口模板，不推荐使用，推荐使用抽象类模板 [SingletonTemplateClass]
 * 缺陷：
 * 1. instance 无法私有化，可以被外部类访问到，意味着它的值可能会被篡改
 * 2. 要显式为 instance 添加 @Volatile 注解
 */
interface SingletonTemplateInterface<in P, out T> {
    //接口属性
    var instance: @UnsafeVariance T?
    fun createInstance(p: P): T

    //接口方法默认实现
    fun getInstance(p: P): T = instance ?: synchronized(this) {
        instance ?: createInstance(p).also {
            instance = it
        }
    }
}


/**
 * 缺陷：必须提前确定构造方法的参数数量和类型
 */
abstract class SingletonTemplateClass<in P, out T> {
    //接口属性
    @Volatile private var instance: T? = null
    abstract fun createInstance(p: P): T

    //接口方法默认实现
    fun getInstance(p: P): T = instance ?: synchronized(this) {
        instance ?: createInstance(p).also {
            instance = it
        }
    }
}


/**
 * 抽象类模板使用
 */
class SingletonUseClassSample private constructor(param: String) {
    companion object : SingletonTemplateClass<String, SingletonUseClassSample>() {
        override fun createInstance(p: String): SingletonUseClassSample {
            return SingletonUseClassSample(p)
        }
    }
}


/**
 * 接口模板使用
 */
class SingletonUseInterfaceSample private constructor(param: String) {
    companion object : SingletonTemplateInterface<String, SingletonUseInterfaceSample> {

        @Volatile override var instance: SingletonUseInterfaceSample? = null
        override fun createInstance(p: String): SingletonUseInterfaceSample {
            return SingletonUseInterfaceSample(p)
        }
    }
}