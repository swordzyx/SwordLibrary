package com.example.userinterface

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.utilclass.LogUtil

class FireMissilesDialogActivity : FragmentActivity(), FireMissilesDialogFragment.NoticeDialogListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun showNoticeDialog() {
        val dialog = FireMissilesDialogFragment()
        dialog.show(supportFragmentManager, "NoticeDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        LogUtil.debug("click positive click on ${dialog.tag}")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        LogUtil.debug("click negative click on ${dialog.tag}")
    }

}