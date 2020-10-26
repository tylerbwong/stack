package me.tylerbwong.stack.screenshot

import com.facebook.testing.screenshot.Screenshot
import me.tylerbwong.stack.integration.BaseIntegrationTest
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import org.junit.Test

class QuestionDetailActivityScreenshotTest : BaseIntegrationTest<QuestionDetailActivity>(
    QuestionDetailActivity::class.java,
    shouldAutomaticallyLaunchActivity = false
) {

    @Test
    fun captureQuestionDetailActivity() {
        val intent = QuestionDetailActivity.makeIntent(
            targetContext,
            id = 354390,
            deepLinkSite = "meta.stackexchange.com"
        )
        launchActivity(intent)
        waitForRequest()
        Screenshot.snapActivity(activity).record()
    }
}
