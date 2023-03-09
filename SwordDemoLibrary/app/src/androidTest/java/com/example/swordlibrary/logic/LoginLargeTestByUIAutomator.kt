package com.example.swordlibrary.logic

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObject
import androidx.test.uiautomator.Until
import okhttp3.internal.wait
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

    
@RunWith(AndroidJUnit4::class)
class LoginLargeTestByUIAutomator {
  private val launchTimeout = 5000L
  private lateinit var launchPackage: String
  private lateinit var uiDevice: UiDevice
  @Before
  fun startActivityFromHome() {
    uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    //回到桌面
    uiDevice.pressHome()
    launchPackage = uiDevice.launcherPackageName
    MatcherAssert.assertThat(launchPackage, Matchers.notNullValue())
    uiDevice.wait(Until.hasObject(By.pkg(launchPackage).depth(0)), launchTimeout)
  }
  
  @Test
  fun should_show_username_in_main_activity_when_login_success() {
    //输入正确的账户名
    uiDevice.findObject(By.res(launchPackage, "username")).text = "123@163.com"
    uiDevice.findObject(By.res(launchPackage, "password")).text = "123456"
    uiDevice.findObject(By.res(launchPackage, "login")).click()
    val uiObject = uiDevice.wait(Until.findObject(By.res(launchPackage, "text")), 500)
    assertEquals(uiObject.text, "123")
  }
}