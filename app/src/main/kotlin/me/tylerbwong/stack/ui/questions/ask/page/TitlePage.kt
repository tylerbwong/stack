package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Companion.TITLE_LENGTH_MAX
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Companion.TITLE_LENGTH_MIN

@Composable
fun TitlePage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    AskQuestionDetailsLayout(
        title = stringResource(R.string.title),
        description = stringResource(R.string.title_page_description, TITLE_LENGTH_MIN),
    ) {
        OutlinedTextField(
            value = viewModel.title,
            onValueChange = viewModel::updateTitle,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.title_page_hint)) },
            supportingText = {
                Text(
                    text = "${viewModel.title.length} / $TITLE_LENGTH_MAX",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                )
            }
        )
    }
}
