package com.sword.constraintlayout

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.group.*
import kotlinx.android.synthetic.main.helpers.*
import kotlinx.android.synthetic.main.layer.*

class Helpers: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.helpers)
    }

    fun click(view: View) {
        when(view.id) {
            R.id.group_layout -> {
                changeLayout(R.layout.group)
                buttonGroup.setOnClickListener{
                    group.visibility = View.GONE
                }
            }
            R.id.layer_layout -> {
                changeLayout(R.layout.layer)
                buttonLayer.setOnClickListener{
                    layer.rotation = 45f
                    layer.translationX = 100f
                    layer.translationY = 100f
                }
            }
            R.id.barrier_layout -> {
                changeLayout(R.layout.barrier)
            }
        }
    }

    private fun changeLayout(group: Int) {
        var constraintSet = ConstraintSet().apply {
            isForceId = false
            clone(this@Helpers, group)
        }
        constraintSet.applyTo((helpers as ConstraintLayout))
    }
}