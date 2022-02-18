package me.tylerbwong.stack.integration

import android.content.ComponentName
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.settings.SettingsActivity
import org.junit.Test

class MainActivityIntegrationTest : BaseIntegrationTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testMainActivityLaunches() {
        waitForRequest()
        assertDisplayed(R.string.questions)
    }

    @Test
    fun testClickingSearchShowsSearch() {
        clickMenu(R.id.search)
        waitForRequest()
        assertDisplayed(R.string.search)
    }

    @Test
    fun testClickingQuestionOpensQuestionDetailActivity() {
        waitForRequest()
        clickListItem(R.id.recyclerView, 2)
        intended(hasComponent(ComponentName(targetContext, QuestionDetailActivity::class.java)))
    }

    @Test
    fun testClickingSettingsOpensSettingsActivity() {
        clickMenu(R.id.settings)
        intended(hasComponent(ComponentName(targetContext, SettingsActivity::class.java)))
    }

    @Test
    fun testClickingProfileOpensLogInDialog() {
        clickOn(R.id.profileIcon)
        assertDisplayed(R.string.log_in_title)
    }
}
