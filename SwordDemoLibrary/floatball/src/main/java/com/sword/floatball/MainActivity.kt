package com.sword.floatball

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.utilclass.LogUtil

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), OnMenuItemClickListener {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    FloatBall.getInstance(this).showFloatBall()
    FloatBall.getInstance(this).setFloatMenuItemListener(this)
  }

  override fun onMenuItemClick(index: Int) {
    when (index) {
      FloatBall.FLOAT_ITEM_USER -> {
        showUserActivity()
        LogUtil.debug("$TAG - click user item")
      }
      FloatBall.FLOAT_ITEM_FEEDBACK -> {
        LogUtil.debug("$TAG - click feedback item")
        showFeedBackDialog()
      }
      FloatBall.FLOAT_ITEM_GIFT -> {
        LogUtil.debug("$TAG - click gift item")
      }
      FloatBall.FLOAT_ITEM_SWITCH_ACCOUNT -> {
        LogUtil.debug("$TAG - click switch account item")
      }
    }
    TODO("Not yet implemented")
  }

  private fun showUserActivity() {
    val intent = Intent(this, UserActivity::class.java);
    startActivity(intent)
  }

  private fun showFeedBackDialog() {
    val intent = Intent(this, WebViewActivity::class.java)
    startActivity(intent)
    //val dialog = FloatMenuDialog(this, )
  }
}