package com.example.swordlibrary.kotlin

import android.content.Context
import android.database.DataSetObserver
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.swordlibrary.R

class MainActivity : AppCompatActivity() , View.OnClickListener {
    val TAG = "zero"
    var editText: EditText? = null
    var linearLayout: LinearLayout? = null
    var listPopupWindow: ListPopupWindow? = null
    val phoneInfos = arrayOf("181****3573", "182****3573", "183****3573")

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_sample)

        editText = findViewById(R.id.phone_number)
        linearLayout = findViewById(R.id.spinner_root)
        findViewById<ImageView>(R.id.spinner_item_click).setOnClickListener(this)

        createPopupListWindow()
    }

    private fun createPopupListWindow() {
        listPopupWindow = ListPopupWindow(this).also { window ->
            window.setAdapter(ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown, R.id.phone_number_tv, phoneInfos))
//            window.setAdapter(CustomAdapter(this))
            window.anchorView = linearLayout
            window.isModal = true
            window.setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            listPopupWindow.setOnItemSelectedListener(onItemClickListener)
        }
    }

    override fun onClick(v: View?) {
        if (!listPopupWindow?.isShowing!!) {
            listPopupWindow!!.verticalOffset = -editText?.height!!
            listPopupWindow?.show()
        }
    }


    inner class CustomAdapter(val context: Context, var dropDownViewId: Int = R.layout.simple_spinner_dropdown) : BaseAdapter(){
        val phoneInfos = arrayOf("181****3573", "181****3573", "181****3573")

        override fun getCount(): Int {
            return phoneInfos.size
        }

        override fun getItem(position: Int): Any {
            return phoneInfos[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
            var viewHolder: ViewHolder
            var view = convertView

            if (view == null) {
                view = View.inflate(context, dropDownViewId, null)
                viewHolder = ViewHolder(view)
            } else {
                viewHolder = view.getTag() as ViewHolder
            }
            viewHolder.phoneNumber.text = phoneInfos[position]
            return view
        }

        fun setDropDownViewResource(resId: Int) {
            dropDownViewId = resId
        }

        inner class ViewHolder(view: View) {
            val phoneNumber = view.findViewById<TextView>(R.id.phone_number_tv)

            init {
                view.setTag(this)
            }
        }

    }

    val onItemClickListener = object: AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            editText?.setText(phoneInfos[position])
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }


    }

    fun deleteToken(view: View) {
        Log.d("Sword", "delete token")
    }


}