package com.example.swordlibrary.kotlin

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.swordlibrary.R

class ListPopupWindowWithEditTextImage(val phoneInfos: Array<String>) : View.OnClickListener {
    var editText: EditText? = null
    var linearLayout: LinearLayout? = null
    var listPopupWindow: ListPopupWindow? = null

    fun showSpinnerWithEditTextImageView(activity: Activity) {
        editText = activity.findViewById(R.id.phone_number)
        linearLayout = activity.findViewById(R.id.spinner_root)
        activity.findViewById<ImageView>(R.id.spinner_item_click).setOnClickListener(this)


        createPopupListWindow(activity)
    }

    private fun createPopupListWindow(context: Context) {
        listPopupWindow = ListPopupWindow(context).also { window ->
//            window.setAdapter(ArrayAdapter<String>(context, R.layout.simple_spinner_dropdown, R.id.phone_number_tv, phoneInfos))
            window.setAdapter(CustomAdapter(context))
            window.anchorView = linearLayout
            window.isModal = true
            window.setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            listPopupWindow?.setOnItemSelectedListener(onItemClickListener)
        }
    }

    override fun onClick(v: View?) {
        if (!listPopupWindow?.isShowing!!) {
            listPopupWindow!!.verticalOffset = -editText?.height!!
            listPopupWindow?.show()
        }
    }


    val onItemClickListener = object: AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            editText?.setText(phoneInfos[position])
            listPopupWindow?.dismiss()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }


    }
}