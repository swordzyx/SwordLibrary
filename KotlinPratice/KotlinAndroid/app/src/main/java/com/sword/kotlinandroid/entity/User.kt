package com.sword.kotlinandroid.entity

class User {
    private var username: String? = null
    private var password: String? = null
    private var code: String? = null

    constructor() {

    }

    constructor(username: String, password: String, code: String) {
        this.username = username
        this.password = password
        this.code = code
    }
}