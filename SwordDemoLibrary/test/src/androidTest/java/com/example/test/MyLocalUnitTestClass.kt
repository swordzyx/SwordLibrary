package com.example.test

import android.content.Context
import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.*


private const val SP_NAME = "test"
private const val FAKE_STRING = "HELLO_WORLD"
private const val TEST_NAME = "Test name"
private const val TEST_EMAIL = "test@email.com"
private val TEST_BIRTH = Calendar.getInstance()

@RunWith(MockitoJUnitRunner::class)
class MyLocalUnitTestClass {


    @Mock
    private lateinit var context: Context
    @Mock
    private lateinit var mMockSharedPreferences: SharedPreferences

    @Test
    fun readStringFromContext_LocalizedString() {
        `when`(context.getString(R.string.hello_world)).thenReturn(FAKE_STRING)
        assertThat(getHelloWorldString(context)).isEqualTo(FAKE_STRING)
        //val entry = SharedPreferenceEntry(TEST_NAME, TEST_BIRTH, TEST_EMAIL)
        //val spHelper = SharedPreferencesHelper(mMockSharedPreferences)
        //
        //assertThat(spHelper.savePersonalInfo(entry)).isTrue()
        //
        //val spEntry = spHelper.personalInfo
        //assertThat(spEntry.name).isEqualTo(TEST_NAME)
        //assertThat(spEntry.email).isEqualTo(TEST_EMAIL)
        //assertThat(spEntry.birth).isEqualTo(TEST_BIRTH)
    }

    fun getHelloWorldString(context: Context): String {
        return "Hello world"
    }
}