package com.example.swordlibrary.kotlin

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.swordlibrary.R

class CustomAdapter(val context: Context, var dropDownViewId: Int = R.layout.simple_spinner_dropdown, var textViewId: Int = R.id.phone_number_tv): BaseAdapter() {
    val phoneInfos = arrayOf("181****3573", "181****3573", "181****3573")
    var onTouchListener: View.OnTouchListener? = null

    override fun getCount(): Int {
        return phoneInfos.size
    }

    override fun getItem(position: Int): Any {
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
        } else {
            viewHolder = view.getTag() as ViewHolder
        }
        viewHolder.phoneNumber.text = phoneInfos[position]
        if (onTouchListener != null) {
            Log.d("Sword", "setOnTouchListener popup item")
            viewHolder.phoneNumber.setOnTouchListener(onTouchListener)
        }
        return view
    }


    inner class ViewHolder(view: View) {
        val phoneNumber = view.findViewById<TextView>(textViewId)

        init {
            view.setTag(this)
        }
    }
}