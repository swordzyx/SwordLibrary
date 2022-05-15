package com.example.learnkotlin.entity

/**
 * 将三个参数的构造器声明为主构造器，然后在空参构造器中调用主构造器，传入 null
 *
 * username、password、code 会自动成为 User2 的成员属性
 */
class User2 constructor(var username: String?, var password: String?, var code: String?) {

    /**
     * 主构造器之外的构造器需要通过 this(...) 显式调用主构造器，否则会报错
     * User2 的主构造器是空参构造，因此括号里面不需要传入任何东西。
     */
     constructor() : this(null, null, null)
}