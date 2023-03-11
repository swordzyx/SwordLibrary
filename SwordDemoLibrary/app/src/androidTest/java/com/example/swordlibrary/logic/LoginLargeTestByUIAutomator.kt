package com.example.swordlibrary.logic

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.example.swordlibrary.MainActivity
import com.sword.LogUtil
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginLargeTestByUIAutomator {
  private val tag = "UIAutomator"
  private val launchTimeout = 5000L
  private lateinit var launchPackage: String
  private lateinit var uiDevice: UiDevice
  private val basicPackage = "com.example.swordlibrary"
  @Before
  fun startActivityFromHome() {
    uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    //回到桌面
    uiDevice.pressHome()
    launchPackage = uiDevice.launcherPackageName
    LogUtil.debug(tag,"launchPackage: $launchPackage")
    MatcherAssert.assertThat(launchPackage, Matchers.notNullValue())
    uiDevice.wait(Until.hasObject(By.pkg(launchPackage).depth(0)), launchTimeout)
    
    //启动目标 APP ，目标 APP 需手动开启“允许从后台弹出界面”的权限。
    val context = ApplicationProvider.getApplicationContext<Context>()
    val intent = context.packageManager.getLaunchIntentForPackage(basicPackage)?.apply {
      addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    LogUtil.debug(tag, intent.toString())
    context.startActivity(intent)
    //uiDevice.findObject(By.text("SwordDemoLibrary")).click()
    //等待应用启动
    uiDevice.wait(Until.hasObject(By.pkg(basicPackage).depth(0)), launchTimeout * 15)
  }
  
  @Test
  fun should_show_username_in_main_activity_when_login_success() {
    val userNameSelector = By.desc("账号")
    val passwordSelector = By.desc("密码")
    val loginButtonSelector = By.desc("登录")
    //输入正确的账户名
    uiDevice.findObject(userNameSelector).apply {
      text = "123@163.com"
    }
    uiDevice.findObject(passwordSelector).apply {
      text = "123456"
    }
    uiDevice.findObject(loginButtonSelector).apply {
      click()
    }
    assert(uiDevice.findObject(userNameSelector) == null)
    assert(uiDevice.findObject(passwordSelector) == null)
    assert(uiDevice.findObject(loginButtonSelector) == null)
    
    val uiObject = uiDevice.wait(Until.findObject(By.text("登录成功")), 500)
    assertEquals(uiObject.text, "登录成功")
  }
}