package com.example.userinterface

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.ListAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.utilclass.LogUtil
import com.example.utilclass.toast
import java.lang.IllegalStateException

class FireMissilesDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        /**
         * setMessage 和 setItems 不能同时调用
         */
        return activity.let { 
            AlertDialog.Builder(it).apply { 
                //标题
                setTitle(R.string.title)
                //内容
                setMessage(R.string.dialog_fire_missiles)
                //在 Dialog 中显示单选列表，setItems 和 setMessage 不能同时调用
                setItems(R.array.color_array, DialogInterface.OnClickListener { dialog, id ->
                    toast(context, "select $id")
                })
                
                //操作按钮
                setPositiveButton(R.string.fire) {dialog, id ->
                    LogUtil.debug("click positive button, id = $id")
                }
                setNegativeButton(R.string.cancel) { dialog, id ->
                    LogUtil.debug("click negative button, id  = $id")
                }
            }.create() ?: throw IllegalStateException("Dialog 创建失败")
        }
    }
    
    
    //在 Dialog 中显示列表
    fun buildListDialog(items: Array<String> = arrayOf("Red", "Green", "Blue"), title: String = "title") {
        val singleChoiceListDialog = AlertDialog.Builder(context).apply { 
            setTitle(title)
            setItems(items) { dialog, id ->
                //列表点击事件监听器
                LogUtil.debug("click id $id")
            }
        }.create()
        
        val multipleChoiceDialog = AlertDialog.Builder(context).apply { 
            setTitle(title)
            setMultiChoiceItems(items, null) { dialog, which, isChecked ->
                
            }
        }
    }
}