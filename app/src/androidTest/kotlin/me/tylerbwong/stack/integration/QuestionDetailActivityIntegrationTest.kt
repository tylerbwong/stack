package me.tylerbwong.stack.integration

import android.content.ComponentName
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.clickMenu
import com.schibsted.spain.barista.interaction.BaristaMenuClickInteractions.openMenu
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import org.junit.Test

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
        assertDisplayed(R.id.answerBody)
    }

    private fun navigateToQuestionDetailsFromMainActivity() {
        waitForRequest()
        clickMenu(R.id.sort)
        clickOn(R.string.votes)
        waitForRequest()
        clickListItem(R.id.recyclerView, 1)
    }
}
