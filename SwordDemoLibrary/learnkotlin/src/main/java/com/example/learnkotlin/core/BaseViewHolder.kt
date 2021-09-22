package com.example.learnkotlin.core

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

open class BaseViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var viewHashMap = hashMapOf<Int, View>()

    protected fun <T : View> getView(@IdRes id: Int): T {
        var view = viewHashMap[id]
        if (view == null) {
            view = itemView.findViewById(id)
            viewHashMap[id] = view
        }
        return view as T
    }

    protected fun setText(@IdRes id: Int, text: String?) {
        (viewHashMap[id] as TextView).text = text
    }
    
}