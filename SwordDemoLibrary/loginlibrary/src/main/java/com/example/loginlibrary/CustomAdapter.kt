package com.example.loginlibrary

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CustomAdapter(val context: Context, var phoneInfos: Array<String>, var dropDownViewId: Int = R.layout.list_popup_window_item_textview_only, var textViewId: Int = R.id.popup_list_item_textview): BaseAdapter() {
    var onTouchListener: View.OnTouchListener? = null
    var drawableRight: Drawable? = null

    override fun getCount(): Int {
        return phoneInfos.size
    }

    override fun getItem(position: Int): Any? {
        return phoneInfos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var viewHolder: ViewHolder
        var view = convertView

        if (view == null) {
            view = View.inflate(context, dropDownViewId, null)
            viewHolder = ViewHolder(view)

            if (drawableRight != null) {
                viewHolder.phoneNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null)
            }

            if (onTouchListener != null) {
                Log.d("Sword", "setOnTouchListener popup item")
                viewHolder.phoneNumber.setOnTouchListener(onTouchListener)
            }
        } else {
            viewHolder = view.getTag() as ViewHolder
        }
        viewHolder.phoneNumber.text = phoneInfos[position]

        return view
    }


    inner class ViewHolder(view: View) {
        val phoneNumber = view.findViewById<TextView>(textViewId)

        init {
            view.setTag(this)
        }
    }
}