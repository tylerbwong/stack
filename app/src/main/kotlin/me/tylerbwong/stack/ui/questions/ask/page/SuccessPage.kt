package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.runtime.Composable

@Composable
fun SuccessPage() {
    AskQuestionDetailsLayout(
        title = "Question posted successfully",
        description = "Nice! Your question has been asked."
    )
}
