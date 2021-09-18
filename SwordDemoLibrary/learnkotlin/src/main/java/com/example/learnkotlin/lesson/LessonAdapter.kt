package com.example.learnkotlin.lesson

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnkotlin.core.BaseViewHolder

class LessonAdapter : RecyclerView.Adapter<LessonAdapter.LessonViewHolder>() {
    
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    /**
     * kotlin 中没有显示修饰符的嵌套类和 Java 中的 static 嵌套类是一样的
     */
    class LessonViewHolder(item: View) : BaseViewHolder(item) {
    
    }
}