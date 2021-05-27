package com.example.swordlibrary.kotlin

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.example.swordlibrary.R

class SpinnerSample(val activity: Activity) {

    fun spinnerSample() {

        val phoneInfos = arrayOf("181****3573", "181****3573", "181****3573")
        val spinner = activity.findViewById<Spinner>(R.id.spinner)

        /*ArrayAdapter(this, android.R.layout.simple_spinner_item, phoneInfos).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }*/

        /*ArrayAdapter(this, R.layout.sample_spinner_item, R.id.phone_number, phoneInfos).also { adapter ->
            adapter.setDropDownViewResource(R.layout.sample_spinner_item)
            spinner.adapter = adapter
        }*/

        CustomAdapter(activity, R.layout.sample_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown)
            spinner.adapter = adapter
        }
    }

    inner class CustomAdapter(val context: Context, var dropDownViewId: Int = android.R.layout.simple_spinner_dropdown_item) : BaseAdapter(),
        SpinnerAdapter {
        val phoneInfos = arrayOf("181****3573", "181****3573", "181****3573")

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
            var view: View? = convertView
            if (view == null) {
                view = View.inflate(context, R.layout.sample_spinner_item, null)
            }
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var viewHolder: ViewHolder
            var view = convertView

            if (view == null) {
                viewHolder = ViewHolder(View.inflate(context, dropDownViewId, null))
            } else {
                viewHolder = view.getTag() as ViewHolder
            }
            viewHolder.phoneNumber.text = phoneInfos[position]
            return view
        }

        fun setDropDownViewResource(resId: Int) {
            dropDownViewId = resId
        }

        inner class ViewHolder(view: View) {
            val phoneNumber = view.findViewById<TextView>(R.id.phone_number_tv)

            init {
                view.setTag(this)
            }
        }

    }
}