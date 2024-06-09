package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.compose.MarkdownText
import me.tylerbwong.stack.ui.utils.format

@Composable
fun DuplicateQuestionPage() {
    val context = LocalContext.current
    val viewModel = viewModel<AskQuestionViewModel>()
    val searchSimilar by remember { derivedStateOf { viewModel.title + viewModel.selectedTags } }
    LaunchedEffect(searchSimilar) {
        viewModel.searchSimilar()
    }
    val similarQuestions by viewModel.similarQuestions.observeAsState(initial = emptyList())
    AskQuestionDetailsLayout(
        title = stringResource(R.string.duplicate_page_title),
        description = stringResource(
            if (similarQuestions.isNotEmpty()) {
                R.string.duplicate_page_description
            } else {
                R.string.duplicate_page_description_none
            }
        ),
    ) {
        similarQuestions.forEachIndexed { index, item ->
            QuestionItem(question = item) {
                QuestionDetailActivity.startActivity(context, item.questionId)
            }
            if (index < similarQuestions.lastIndex) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun QuestionItem(question: Question, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Filled.QuestionAnswer,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = question.answerCount.toLong().format(),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            MarkdownText(
                markdown = question.title,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}
