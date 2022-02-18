package me.tylerbwong.stack.integration

import android.content.ComponentName
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.adevinta.android.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.openMenu
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import org.junit.Test
import me.tylerbwong.stack.api.R as ApiR

class QuestionDetailActivityIntegrationTest : BaseIntegrationTest<MainActivity>(
    MainActivity::class.java
) {
    @Test
    fun testQuestionDetailActivityLaunches() {
        navigateToQuestionDetailsFromMainActivity()
        waitForRequest()
        assertDisplayed(R.id.questionTitle)
    }

    @Test
    fun testLinkedQuestionsActivityLaunches() {
        navigateToQuestionDetailsFromMainActivity()
        waitForRequest()
        openMenu()
        clickOn(R.string.linked)
        intended(hasComponent(ComponentName(targetContext, QuestionsActivity::class.java)))
    }

    @Test
    fun testRelatedQuestionsActivityLaunches() {
        navigateToQuestionDetailsFromMainActivity()
        waitForRequest()
        openMenu()
        clickOn(R.string.related)
        intended(hasComponent(ComponentName(targetContext, QuestionsActivity::class.java)))
    }

    @Test
    fun testQuestionDetailAnswersAreShown() {
        navigateToQuestionDetailsFromMainActivity()
        waitForRequest()
        scrollListToPosition(R.id.recyclerView, 3)
        assertDisplayed(R.id.markdownTextView)
    }

    private fun navigateToQuestionDetailsFromMainActivity() {
        waitForRequest()
        clickMenu(R.id.sort)
        clickOn(ApiR.string.votes)
        waitForRequest()
        clickListItem(R.id.recyclerView, 1)
    }
}
