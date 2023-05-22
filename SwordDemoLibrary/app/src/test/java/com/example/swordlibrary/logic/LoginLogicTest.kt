package com.example.swordlibrary.logic

import org.junit.Assert
import org.junit.Test
import sword.logic.LoginLogic

class LoginLogicTest {
  @Test
  fun should_return_false_when_password_is_less_than_5() {
    val logic = LoginLogic()
    val password = "1234"
    val result = logic.isPasswordValid(password)
    Assert.assertFalse(result)
  }
  
  @Test
  fun should_return_false_when_password_is_equals_5() {
    val logic = LoginLogic()
    val password = "12345"
    val result = logic.isPasswordValid(password)
    Assert.assertFalse(result)
  }

  @Test
  fun should_return_false_when_password_is_greater_than_5() {
    val logic = LoginLogic()
    val password = "123456"
    val result = logic.isPasswordValid(password)
    Assert.assertTrue(result)
  }
}