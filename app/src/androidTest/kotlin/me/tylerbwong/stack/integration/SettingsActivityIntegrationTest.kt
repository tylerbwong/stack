package me.tylerbwong.stack.integration

import android.content.ComponentName
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.facebook.testing.screenshot.Screenshot
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.settings.SettingsActivity
import me.tylerbwong.stack.ui.settings.sites.SitesActivity
import org.junit.Ignore
import org.junit.Test

class SettingsActivityIntegrationTest : BaseIntegrationTest<SettingsActivity>(
    SettingsActivity::class.java
) {
    @Test
    fun captureSettingsActivity() {
        waitForRequest()
        Screenshot.snapActivity(activity).record()
    }

    @Test
    fun testLogInDialogIsShown() {
        clickOn(R.string.log_in)
        assertDisplayed(R.string.log_in_title)
    }

    @Test
    fun testTheme() {
        clickOn(R.string.theme)
        assertDisplayed(R.string.theme_title)
        clickOn(R.string.theme_dark)
    }

    @Test
    fun testSitesActivityIsShown() {
        clickOn("Stack Overflow")
        intended(hasComponent(ComponentName(targetContext, SitesActivity::class.java)))
    }
}
