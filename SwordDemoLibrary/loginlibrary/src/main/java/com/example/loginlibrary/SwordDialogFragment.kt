package com.example.loginlibrary

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

class SwordDialogFragment(val resId: Int, val activity: Activity) : DialogFragment() {
    var dialogWidth: Int = WindowManager.LayoutParams.MATCH_PARENT
    var dialogHeight: Int = WindowManager.LayoutParams.MATCH_PARENT


    constructor(resId: Int, activity: Activity, width: Int, height: Int): this(resId, activity) {
        dialogWidth = width
        dialogHeight = height
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(activity)
            .setView(activity.layoutInflater.inflate(resId, null))
            .create()
        return dialog
    }

    override fun onResume() {
        super.onResume()

        setSize(dialogWidth, dialogHeight)

        //dialog?.apply { SpinnerConfigure(activity, findViewById(R.id.spinner)).spinnerSample() }
    }

    fun setSize(width: Int?, height: Int?) {
        val dialog = requireDialog()
        val lp = dialog.window?.attributes
        lp?.width = width
        lp?.height = height
        dialog.window?.attributes = lp
    }
}