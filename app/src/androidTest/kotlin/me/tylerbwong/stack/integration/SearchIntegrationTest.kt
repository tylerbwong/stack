package me.tylerbwong.stack.integration

import android.content.ComponentName
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotExist
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.clearText
import com.adevinta.android.barista.interaction.BaristaEditTextInteractions.writeTo
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.adevinta.android.barista.interaction.BaristaMenuClickInteractions.clickMenu
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import org.junit.Test

class SearchIntegrationTest : BaseIntegrationTest<MainActivity>(MainActivity::class.java) {

    @Test
    fun basicSearch() {
        navigateToSearch()
        writeTo(R.id.searchEditText, "Android")
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
        waitForRequest()
        assertNotExist(R.string.popular_tags)
        clickListItem(R.id.recyclerView, 3)
        intended(hasComponent(ComponentName(targetContext, QuestionDetailActivity::class.java)))
    }

    @Test
    fun advancedSearch() {
        navigateToSearch()
        writeTo(R.id.searchEditText, "Android")
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
        waitForRequest()
        assertNotExist(R.string.popular_tags)
        clickOn(R.string.add_filters)
        writeTo(R.id.titleContainsEditText, "compose")
        writeTo(R.id.bodyContainsEditText, "text")
        writeTo(R.id.tagsEditText, "android")
        clickOn(R.id.applyFiltersButton)
        waitForRequest()
        assertNotExist(R.string.popular_tags)
        clickListItem(R.id.recyclerView, 3)
        pressBack()
        clickOn(R.string.add_filters)
        clickOn(R.id.clearFiltersButton)
        waitForRequest()
        clearText(R.id.searchEditText)
        onView(withId(R.id.searchEditText)).perform(pressImeActionButton())
        waitForRequest()
        assertDisplayed(R.string.popular_tags)
        intended(hasComponent(ComponentName(targetContext, QuestionDetailActivity::class.java)))
    }

    private fun navigateToSearch() {
        clickMenu(R.id.search)
        waitForRequest()
        assertDisplayed(R.string.search)
    }
}
