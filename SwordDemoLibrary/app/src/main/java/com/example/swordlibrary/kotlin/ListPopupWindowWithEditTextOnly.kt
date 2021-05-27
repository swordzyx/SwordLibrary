package com.example.swordlibrary.kotlin

import android.app.Activity
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import com.example.swordlibrary.R

class ListPopupWindowWithEditTextOnly: View.OnTouchListener {
    var listPopupWindow: ListPopupWindow? = null
    var editText: EditText? = null

    fun createView(activity: Activity) {
        editText = activity.findViewById(R.id.popup_list_edit_only)
        editText?.setOnTouchListener(this)

        listPopupWindow = ListPopupWindow(activity).also { popupWindow ->
            popupWindow.setAdapter(CustomAdapter(activity, R.layout.list_popup_window_item_textview_only, R.id.popup_list_item_textview).apply {
                onTouchListener = this@ListPopupWindowWithEditTextOnly
            })
            popupWindow.anchorView = editText
            popupWindow.isModal = true
            popupWindow.setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    fun showListPopupWindow() {
        listPopupWindow?.verticalOffset = -editText?.height!!
        listPopupWindow?.show()
    }

    private fun deleteToken() {
        Log.d("Sword", "deleteToken")
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when(event.action) {
            MotionEvent.ACTION_UP -> {
                val imageXStart = view.right - (view as TextView).compoundDrawables[2].bounds.width()
                if(event.x > imageXStart && event.x < view.right) {
                    if (!listPopupWindow?.isShowing!!) {
                        showListPopupWindow()
                    } else {
                        deleteToken()
                    }
                    return true
                }
            }
        }
        return false
    }


}