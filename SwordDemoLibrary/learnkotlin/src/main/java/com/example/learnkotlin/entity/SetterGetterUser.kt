package com.example.learnkotlin.entity

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

    
    
}