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
        val phoneInfos = arrayOf("181****3573", "182****3573", "183****3573")

        listPopupWindow = ListPopupWindow(context).also { window ->
            window.setAdapter(PopupWindowCustomAdapter(context, phoneInfos))
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