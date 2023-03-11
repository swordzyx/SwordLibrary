package com.example.swordlibrary.logic

import android.view.View
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.example.swordlibrary.MainActivity
import org.hamcrest.Matchers.*
import org.junit.Test

@MediumTest
class LoginPageViewTestByEspresso {
  @Test
  fun should_show_home_page_when_username_is_email_password_greater_than_5() {
    ActivityScenario.launch(MainActivity::class.java)
    onView(withContentDescription("账号")).perform(typeText("123@163.com"))
    onView(withContentDescription("密码")).perform(typeText("123456"))
    onView(withContentDescription("登录")).perform(click())
    
    onView(withContentDescription("首页")).check(matches(isDisplayed()))
  }
  
  @Test
  fun should_show_login_failed_toast_when_username_is_not_email_password_less_than_5() {
    val launch = ActivityScenario.launch(MainActivity::class.java)
    onView(withContentDescription("账号")).perform(typeText("123"))
    onView(withContentDescription("密码")).perform(typeText("1234"))
    onView(withContentDescription("登录")).perform(click())

    var decorView: View? = null
    launch.onActivity { 
      decorView = it.window.decorView
    }
    
    onView(allOf(withText("123 登录失败"))).inRoot(withDecorView(`is`(not(decorView)))).check(matches(isDisplayed()))
    //ShadowToast.getTextofLatestToast.inRoot(withDecorView(not(`is`(decorView))))
  }
}