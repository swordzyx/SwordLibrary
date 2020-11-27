package com.sword.constraintlayout

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addButton()
    }

    /*fun click(v: View) {
        when(v.id) {
            R.id.circulPosition -> {
                startActivity(Intent(this@MainActivity, CircularPositioning::class.java))
            }
            R.id.circularReveal -> {
                startActivity(Intent(this@MainActivity, CircularReveal::class.java))
            }
            R.id.constraintSet -> {
                startActivity(Intent(this@MainActivity, ConstraintSetX::class.java))
            }
            R.id.transitionManager -> {
                startActivity(Intent(this@MainActivity, ConstraintX1::class.java))
            }
            R.id.helpers -> {
                startActivity(Intent(this@MainActivity, Helpers::class.java))
            }
            R.id.linear -> {
                startActivity(Intent(this@MainActivity, Linear::class.java))
            }
        }
    }*/

    fun addButton() {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).activities
            .filterNot { it.name == this::class.java.name }
            .map { Class.forName(it.name) }
            .forEach { clazz ->
                root.addView(AppCompatButton(this).apply {
                    isAllCaps = false
                    text = clazz.simpleName
                    setOnClickListener {
                        startActivity(Intent(this@MainActivity, clazz))
                    }
                })
            }
    }


}