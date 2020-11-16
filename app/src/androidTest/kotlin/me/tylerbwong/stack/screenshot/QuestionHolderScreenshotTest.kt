package me.tylerbwong.stack.screenshot

import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.questions.QuestionHolder
import me.tylerbwong.stack.utils.rawResourceOfType
import org.junit.Ignore
import org.junit.Test

@Ignore("Disabled until standalone @AndroidEntryPoint screenshot tests are fixed")
class QuestionHolderScreenshotTest : BaseScreenshotTest() {

    @Test
    fun captureQuestionHolder() {
        val holder = QuestionHolder(container)
        val question = targetContext.rawResourceOfType<Question>(R.raw.question_item)
        val item = QuestionItem(question)
        holder.bind(item)
        holder.itemView.capture()
    }
}
