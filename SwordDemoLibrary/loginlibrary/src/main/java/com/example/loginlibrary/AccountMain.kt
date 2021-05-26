@file:JvmName("AccountMain")

package com.example.loginlibrary

import androidx.fragment.app.FragmentActivity

fun register(activity: FragmentActivity) {
    val dialogFragment = SwordDialogFragment(R.layout.xlcwsdk_user_register_dialog_layout, activity)
    dialogFragment.show(activity.supportFragmentManager, "regisger_dialog")
}
