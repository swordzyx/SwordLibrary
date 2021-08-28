package com.example.test

import android.content.Context
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

private const val FAKE_STRING = "HELLO_WORLD"

@RunWith(MockitoJUnitRunner::class)
class MyLocalUnitTestClass {

    @Mock
    private lateinit var context: Context

    @Before
    fun initMocks() {
        given(context.getString(R.string.hello_world)).willReturn(FAKE_STRING)
    }

    @Test
    fun readStringFromContext_LocalizedString() {
        val classUnderTest = ClassUnterTest(context)
        assertThat(classUnderTest.getHelloWorldString()).isEqualTo(FAKE_STRING)
    }
}