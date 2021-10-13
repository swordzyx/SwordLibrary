package com.example.learnkotlin.entity

/**
 * 如果使用 constructor 主动声明了构造器，此处继承父类时就不需要在父类后面加括号。
 */
class SetterGetterUser : Any {
    /**
     * kotlin 会自动生成一个私有的 username 成员变量，以及 username 成员的 getter 和 setter 方法
     */
    var username: String? = null

    /**
     * kotlin 会生成一个公开的 password 成员变量，不会生成 setter 和 getter 方法
     */
    @JvmField
    var password: String? = null

    /**
     * val v = code 会默认调用 code 的 get 方法
     * code = "xx" 会默认调用 code 的 set 方法
     * 
     * field 是一个内部属性，用来保存成员的值，防止递归的调用 getter 和 setter 方法。
     * 
     * 以下的 getter 和 setter 代码是默认实现，可以省略
     */
    var code: String? = null
        set(value) {
            field = value
        }
        get() {
            return field
        }

    /**
     * 默认构造函数
     */
    constructor()

    /**
     * 通过 this(...) 调用同一个类中的其他构造器
     */
    constructor(username: String?) : this(username, "", "")

    /**
     * 通过 super(...) 调用父类的构造器
     */
    constructor(username: String?, password: String?, code: String?) : super() {
        this.username = username
        this.password = password
        this.code = code
    }


    /**
     * 创建数组
     */
    private val codeList = arrayOf(
        "kotlin",
        "android",
        "java",
        "http",
        "https",
        "okhttp",
        "retrofit",
        "tcp/ip"
    )

    fun arraySample() {
        /**
         * int 类型的数组，使用 arrayOf(...) 方式创建基本数据类型的数组，会有一个自动装箱拆箱的操作，这会带来额外的开销
         *
         * Kotlin 为基本数据类型提供了专门的函数用于创建数组
         *   int - intArrayOf
         *   float - floatArrayOf
         *   ....
         *
         * kotlin 中的包装类型是可空的，而基本数据类型对应的是不可空的数据类型
         */
        val arrayOf = arrayOf(1, 2, 3, 4, 6)
        val intArrayOf = intArrayOf(1, 2, 3, 4, 5)
    }
}