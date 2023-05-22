package com.example.swordlibrary.logic

import androidx.test.platform.app.InstrumentationRegistry
import sword.data.SharedPreferencesUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import sword.logic.LoginLogic

@RunWith(RobolectricTestRunner::class)
class LoginPageViewTestByRobolectric {
  private val context = InstrumentationRegistry.getInstrumentation().context
  
  @Test
  fun should_return_false_when_given_invalid_username_or_password() {
    val loginLogic = LoginLogic()
    val emptyUserNameResult = loginLogic.login(context, "", "1234567")
    Assert.assertFalse(emptyUserNameResult)
    val emptyPasswordResult = loginLogic.login(context, "123@163.com","")
    Assert.assertFalse(emptyPasswordResult)
  }
  
  @Test
  fun should_return_false_when_given_error_username_or_password() {
    val result = LoginLogic().login(context, "1234", "4678")
    Assert.assertFalse(result)
  }
  
  @Test
  fun should_return_true_when_given_correct_username_password() {
    val username = "123@163.com"
    val password = "123456"
    val result = LoginLogic().login(context, username, password)
    Assert.assertTrue(result)
    //验证缓存信息
    val cachePassword = SharedPreferencesUtils.get(context, username, "")
    Assert.assertEquals(cachePassword, password)
  }
}