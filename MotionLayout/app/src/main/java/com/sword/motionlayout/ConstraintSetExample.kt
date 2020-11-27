package com.sword.motionlayout

import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.activity_constraint_start.*

class ConstraintSetExample: AppCompatActivity(), View.OnClickListener {
    private var toggle = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constraint_start)

        image_film_cover.setOnClickListener(this)
        rating_film_rating.rating = 4.5f
        text_film_title.text = getString(R.string.film_title)
        text_film_description.text = getString(R.string.film_description)
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onClick(v: View) {
        TransitionManager.beginDelayedTransition(root)
        var constraintSet = ConstraintSet()
        if (toggle) {
            constraintSet.clone(this, R.layout.activity_constraint_end)
        } else {
            constraintSet.clone(this, R.layout.activity_constraint_start)
        }
        constraintSet.applyTo(root)
        toggle = !toggle
    }
}