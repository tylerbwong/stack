package me.tylerbwong.stack.integration

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Sets up base integration test properties and rules.
 *
 * This base class should be extended only for integration tests that will be run
 * on a nightly schedule. These tests will make REAL network requests to the
 * Stack Exchange API and should not be run continuously.
 */
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class BaseIntegrationTest<T : Activity>(
    activityClass: Class<T>,
    private val shouldAutomaticallyLaunchActivity: Boolean = true
) {

    @get:Rule
    val testRule = IntentsTestRule(activityClass, false, shouldAutomaticallyLaunchActivity)

    protected val activity: Activity
        get() = testRule.activity

    protected val targetContext: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    protected val context: Context
        get() = InstrumentationRegistry.getInstrumentation().context

    protected fun launchActivity(intent: Intent) {
        if (!shouldAutomaticallyLaunchActivity) {
            testRule.launchActivity(intent)
        }
    }

    @After
    fun finishActivity() {
        if (!shouldAutomaticallyLaunchActivity) {
            testRule.finishActivity()
        }
    }

    // TODO Use IdlingResource instead of Thread.sleep
    protected fun waitForRequest(
        waitTimeMillis: Long = DEFAULT_WAIT_TIME
    ) = Thread.sleep(waitTimeMillis)

    companion object {
        private const val DEFAULT_WAIT_TIME = 2_000L
    }
}
