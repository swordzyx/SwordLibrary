package com.sword.androidarticle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sword.androidarticle.databinding.ActivityViewBindingBinding

/*
build.gradle 须加上
  buildFeatures {
    viewBinding true
  }
*/
class ViewBindingActivity : AppCompatActivity() {
  
  lateinit var binding: ActivityViewBindingBinding
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityViewBindingBinding.inflate(layoutInflater)
    setContentView(binding.root)
    
    binding.text.text = "Hello"
  }
}