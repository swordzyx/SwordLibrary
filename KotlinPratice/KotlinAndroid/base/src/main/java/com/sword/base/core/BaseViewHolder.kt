package com.sword.base.core

import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView


abstract class BaseViewHolder constructor(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val viewHashMap: HashMap<Int, View?> = HashMap()

    protected fun <T: View?> getView(@IdRes id: Int): T?{
        var view = viewHashMap[id]
        if(view == null) {
            view = itemView.findViewById(id)
            viewHashMap[id] = view
        }
        return view as T?
    }

    protected fun setText(@IdRes id: Int, text: String?) {
        getView<TextView>(id)?.text = text
    }
}