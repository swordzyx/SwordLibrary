package com.example.loginlibrary

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

class SwordDialogFragment(val resId: Int, val activity: Activity) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(activity)
            .setView(activity.layoutInflater.inflate(resId, null))
            .create()

        return dialog
    }

    fun setSize(width: Int, height: Int) {
        val lp = dialog?.window?.attributes
        lp?.width = width
        lp?.height = height
        dialog?.window?.attributes = lp
    }

}