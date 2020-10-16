package com.sword.base.core

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView


class BaseViewHolder: RecyclerView.ViewHolder {
    constructor(itemView: View) {
        super(itemView)
    }

    private val viewHashMap: Map<Integer, View> = HashMap()

    protected fun <T: View?> getView(@IdRes id: Int): {
        var view: View = viewHashMap.get(id)
    }
}