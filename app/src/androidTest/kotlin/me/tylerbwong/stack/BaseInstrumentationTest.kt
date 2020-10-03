package me.tylerbwong.stack

import android.app.Activity
import android.content.Context
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
abstract class BaseInstrumentationTest<T : Activity>(activityClass: Class<T>) {

    @get:Rule
    val testRule = IntentsTestRule(activityClass)

    protected val targetContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    protected val context: Context
        get() = InstrumentationRegistry.getInstrumentation().context
}
