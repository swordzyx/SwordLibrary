package com.sword.androidarticle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sword.androidarticle.databinding.FruitItemBinding

class FruitAdapter(val fruitList: List<Fruit>) : RecyclerView.Adapter<FruitAdapter.ViewHolder>(){
  inner class ViewHolder(binding: FruitItemBinding): RecyclerView.ViewHolder(binding.root) {
    val fruitImage = binding.image
    val fruitName = binding.text
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding = FruitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val fruit = fruitList[position]
    holder.fruitImage.setImageResource(fruit.fruitImage)
    holder.fruitName.text = fruit.fruitName
  }

  override fun getItemCount(): Int {
    return fruitList.size
  }
}