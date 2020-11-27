package com.sword.motionlayout

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities.forEach {activity ->
            if (activity.name == this::class.java.name) {
                return@forEach
            }

            val clazz = Class.forName(activity.name)
            val button = Button(this).apply {
                isAllCaps = false
                text = clazz.simpleName
                setOnClickListener {
                    startActivity(Intent(this@MainActivity, clazz))
                }
            }
            root.addView(button)
        }
    }
}