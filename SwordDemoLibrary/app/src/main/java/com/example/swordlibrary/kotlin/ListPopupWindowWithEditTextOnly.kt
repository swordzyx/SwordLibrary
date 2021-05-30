package com.example.swordlibrary.kotlin

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import com.example.loginlibrary.R

class ListPopupWindowWithEditTextOnly: View.OnTouchListener{
    var listPopupWindow: ListPopupWindow? = null
    var editText: EditText? = null

    fun initView(activity: Activity, editTextId: Int) {
        val drawableShanchu = getDrawableWithSize(activity,
            R.drawable.bg_sdk_shanchu_png, false, 20.dp, 20.dp)
        val drawableDropdown = getDrawableWithSize(activity, R.drawable.bg_sdk_xiala, true, 20.dp)

        val phoneInfos = arrayOf("181****3573", "182****3573", "183****3573")

        editText = activity.findViewById(editTextId)
        editText?.setOnTouchListener(this)
        editText?.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableDropdown, null)

        listPopupWindow = ListPopupWindow(activity).also { popupWindow ->
            popupWindow.setAdapter(PopupWindowCustomAdapter(activity, phoneInfos).apply {
                onTouchListener = this@ListPopupWindowWithEditTextOnly
                drawableRight = drawableShanchu
            })
            popupWindow.anchorView = editText
            popupWindow.isModal = true
            popupWindow.setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    private fun showListPopupWindow() {
        listPopupWindow?.verticalOffset = -editText?.height!!
        listPopupWindow?.show()
    }

    private fun deleteToken() {
        Log.d("Sword", "deleteToken")
    }

    private fun getDrawableWithSize(activity: Activity, drawableId: Int, scaleWithWidth: Boolean, width: Float, height: Float = 0f): Drawable? {
        val bitmap = BitmapFactory.decodeResource(activity.resources, drawableId)
        val matrix = Matrix()

        matrix.postScale(width/bitmap.width, if(scaleWithWidth) width/bitmap.width else height/bitmap.height)
        val newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return BitmapDrawable(activity.resources, newBitmap)
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
                } else if(listPopupWindow?.isShowing!!) {
                    editText?.setText(view.text)
                    listPopupWindow?.dismiss()
                }
            }
        }
        return false
    }
}