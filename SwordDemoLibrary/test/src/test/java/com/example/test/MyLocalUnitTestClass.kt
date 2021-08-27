package com.example.test

import android.content.Context
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

private const val FAKE_STRING = "HELLO_WORLD"

@RunWith(MockitoJUnitRunner::class)
class MyLocalUnitTestClass {


    @Mock
    private lateinit var context: Context

    lateinit var closeable: AutoCloseable

    @Before
    fun openMocks() {
        closeable = MockitoAnnotations.openMocks(this)
    }

    @Test
    fun readStringFromContext_LocalizedString() {
        val classUnderTest = ClassUnterTest(context)
        assertThat(classUnderTest.getHelloWorldString()).isEqualTo(FAKE_STRING)
    }

    @After
    fun releaseMocks() {
        closeable.close()
    }
}