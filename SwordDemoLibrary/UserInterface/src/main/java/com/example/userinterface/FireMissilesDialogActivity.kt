package com.example.userinterface

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.utilclass.LogCollector

class FireMissilesDialogActivity : FragmentActivity(), FireMissilesDialogFragment.NoticeDialogListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showNoticeDialog() {
        val dialog = FireMissilesDialogFragment()
        dialog.show(supportFragmentManager, "NoticeDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        LogCollector.debug("click positive click on ${dialog.tag}")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        LogCollector.debug("click negative click on ${dialog.tag}")
    }

}