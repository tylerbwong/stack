package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.tylerbwong.stack.R

@Composable
fun SuccessPage() {
    AskQuestionDetailsLayout(
        title = stringResource(R.string.success_page_title),
        description = stringResource(R.string.success_page_description),
    )
}
