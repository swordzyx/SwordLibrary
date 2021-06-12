package com.example.constraintlayout

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewgroup = findViewById<LinearLayout>(R.id.root_linear)

        if(viewgroup == null) {
            Log.d("sword", "add view")
        }

        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities
            .filterNot { it.name == this::class.java.name }
            .map { Class.forName(it.name) }
            .forEach { clazz ->
                Log.d("sword", "${clazz.simpleName}")
                viewgroup.addView(AppCompatButton(this).apply {
                    text = clazz.simpleName
                    isAllCaps = false
                    setOnClickListener {
                        startActivity(Intent(this@MainActivity, clazz))
                    }
                })
            }
    }
}