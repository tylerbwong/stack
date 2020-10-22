package me.tylerbwong.stack.integration

import android.content.ComponentName
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.profile.ProfileActivity
import org.junit.Test

class ProfileActivityIntegrationTest : BaseIntegrationTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testProfileActivityIsShown() {
        navigateToQuestionDetailsFromMainActivity()
        waitForRequest(2000)
        scrollListToPosition(R.id.recyclerView, 3)
        assertDisplayed(R.id.answerBody)
        clickOn(R.id.userImage)
        waitForRequest(2000)
        assertDisplayed(R.id.questionTitle)
        intended(hasComponent(ComponentName(targetContext, ProfileActivity::class.java)))
    }

    @Test
    fun testBadgesAreShown() {
        navigateToQuestionDetailsFromMainActivity()
        waitForRequest(2000)
        scrollListToPosition(R.id.recyclerView, 3)
        assertDisplayed(R.id.answerBody)
        clickOn(R.id.userImage)
        waitForRequest(2000)
        assertDisplayed(R.id.questionTitle)
        clickMenu(R.id.badges)
        waitForRequest(2000)
        intended(hasComponent(ComponentName(targetContext, ProfileActivity::class.java)))
    }

    private fun navigateToQuestionDetailsFromMainActivity() {
        waitForRequest(2000)
        clickMenu(R.id.sort)
        clickOn(R.string.votes)
        waitForRequest(2000)
        clickListItem(R.id.recyclerView, 1)
    }
}
