package com.sword.motionlayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.security.acl.Group

class ObjectAnimatorActivity : AppCompatActivity(){
    lateinit var root: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_animator)
    }
}