package me.tylerbwong.stack

import android.content.ComponentName
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import org.junit.Test

class MainActivityTest : BaseInstrumentationTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun testMainActivityLaunches() {
        assertDisplayed(R.string.questions)
    }

    @Test
    fun testClickingQuestionOpensQuestionDetailActivity() {
        clickListItem(R.id.recyclerView, 2)
        intended(hasComponent(ComponentName(targetContext, QuestionDetailActivity::class.java)))
    }
}
