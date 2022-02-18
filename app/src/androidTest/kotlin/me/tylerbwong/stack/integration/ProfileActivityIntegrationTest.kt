package me.tylerbwong.stack.integration

import android.content.ComponentName
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.adevinta.android.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.profile.ProfileActivity
import org.junit.Test

class ProfileActivityIntegrationTest : BaseIntegrationTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testProfileActivityIsShown() {
        navigateToQuestionDetailsFromMainActivity()
        waitForRequest(waitTimeMillis = 4_000L)
        scrollListToPosition(R.id.recyclerView, 18)
        assertDisplayed(R.id.markdownTextView)
        clickOn(R.id.userImage)
        waitForRequest(waitTimeMillis = 4_000L)
        assertDisplayed(R.id.questionTitle)
        intended(hasComponent(ComponentName(targetContext, ProfileActivity::class.java)))
    }

    @Test
    fun testBadgesAreShown() {
        navigateToQuestionDetailsFromMainActivity()
        waitForRequest()
        scrollListToPosition(R.id.recyclerView, 18)
        assertDisplayed(R.id.markdownTextView)
        clickOn(R.id.userImage)
        waitForRequest()
        assertDisplayed(R.id.questionTitle)
        clickMenu(R.id.badges)
        waitForRequest()
        intended(hasComponent(ComponentName(targetContext, ProfileActivity::class.java)))
    }

    private fun navigateToQuestionDetailsFromMainActivity() {
        waitForRequest()
        clickMenu(R.id.sort)
        clickOn(me.tylerbwong.stack.api.R.string.votes)
        waitForRequest()
        clickListItem(R.id.recyclerView, 1)
    }
}
