package com.example.loginlibrary

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*

class SpinnerConfigure(val activity: Activity, val spinner: Spinner) {

    fun spinnerSample() {

        val phoneInfos = arrayOf("181****3573", "182****3573", "183****3573")

        CustomAdapter(activity, phoneInfos, R.layout.spinner_layout_textview, R.id.popup_list_edit_only).also { adapter ->
            spinner.adapter = adapter
            spinner.onItemSelectedListener = adapter
            adapter.onTouchListener = touchListener
        }


        ArrayAdapter(activity, R.layout.spinner_layout_textview, phoneInfos).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_layout_textview)
        }
    }

    inner class CustomAdapter(val context: Context, val phoneInfos: Array<String>, var spinnerLayoutId: Int = android.R.layout.simple_spinner_dropdown_item, val spinnerTextViewId: Int = android.R.id.text1 ) : BaseAdapter(), SpinnerAdapter, AdapterView.OnItemSelectedListener {
        var spinnerDropdownLayoutId = spinnerLayoutId
        var dropdownTextViewId = spinnerTextViewId
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
            var view: View? = convertView
            if (view == null) {
                view = View.inflate(context, spinnerLayoutId, null)
                (view as TextView).setCompoundDrawablesWithIntrinsicBounds(null, null, context.resources.getDrawable(R.drawable.bg_sdk_xiala), null)

            }
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var viewHolder: ViewHolder
            var view = convertView

            if (view == null) {
                view = View.inflate(context, spinnerDropdownLayoutId, null)
                viewHolder = ViewHolder(view)
            } else {
                viewHolder = view.getTag() as ViewHolder
            }
            viewHolder.phoneNumber.text = phoneInfos[position]
            return view
        }

        inner class ViewHolder(view: View) {
            val phoneNumber = view.findViewById<TextView>(dropdownTextViewId)

            init {
                view.setTag(this)
                if (onTouchListener != null) {
                    Log.d("Sword", "set onTouchListener")
                    phoneNumber.setOnTouchListener(onTouchListener)
                }
            }
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Log.d("Sword", "select ${position} : ${(view as TextView).text}")
            parent?.findViewById<TextView>(spinnerTextViewId)?.setText(phoneInfos[position])
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

    }

    val touchListener = object : View.OnTouchListener {
        var drawableRightClick = false
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            val imageXStart = view.right - (view as TextView).compoundDrawables[2].bounds.width()
            when(event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if(event.x > imageXStart && event.x < view.right) {
                        drawableRightClick = true
                        return true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if(event.x > imageXStart && event.x < view.right && drawableRightClick) {
                        drawableRightClick = false
                        deleteToken()
                        return true
                    }
                }
            }
            return false
        }

    }

    fun deleteToken() {
        Log.d("Sword", "delete token")
    }
}