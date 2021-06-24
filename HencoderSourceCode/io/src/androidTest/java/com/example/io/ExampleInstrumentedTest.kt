<<<<<<< HEAD
package com.example.io
=======
<<<<<<< HEAD:SwordDemoLibrary/script/src/androidTest/java/com/sword/script/ExampleInstrumentedTest.kt
package com.sword.script
=======
package com.example.io
>>>>>>> 04b3a3d (no description(amend)):HencoderSourceCode/io/src/androidTest/java/com/example/io/ExampleInstrumentedTest.kt
>>>>>>> 04b3a3d (no description(amend))

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
<<<<<<< HEAD
        assertEquals("com.example.io.test", appContext.packageName)
=======
<<<<<<< HEAD:SwordDemoLibrary/script/src/androidTest/java/com/sword/script/ExampleInstrumentedTest.kt
        assertEquals("com.sword.script.test", appContext.packageName)
=======
        assertEquals("com.example.io.test", appContext.packageName)
>>>>>>> 04b3a3d (no description(amend)):HencoderSourceCode/io/src/androidTest/java/com/example/io/ExampleInstrumentedTest.kt
>>>>>>> 04b3a3d (no description(amend))
    }
}