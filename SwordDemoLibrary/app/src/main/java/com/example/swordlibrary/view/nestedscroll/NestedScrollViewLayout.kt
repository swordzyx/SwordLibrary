package com.example.swordlibrary.view.nestedscroll

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import com.example.swordlibrary.R

class NestedScrollViewLayout(context: Context) {
  @SuppressLint("InflateParams")
  val nestedScrollView = LayoutInflater.from(context).inflate(R.layout.nested_scalable_image_view, null)
  
}