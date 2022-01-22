package com.sword.androidarticle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sword.androidarticle.databinding.FragmentViewBindingBinding

class ViewBindingFragment : Fragment(){
  private var binding: FragmentViewBindingBinding? = null
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentViewBindingBinding.inflate(inflater, container, false)
    binding?.text?.text = "FragmentViewBinding"
    return binding?.root
  }
  
  //在 onDestroyView() 中对置空 binding，保证 binding 变量的有效生命周期是在 onCreateView() 和 onDestroyView() 函数之间。 
  override fun onDestroyView() {
    super.onDestroyView()
    binding = null
  }
}