@file:Suppress("MagicNumber")
package me.tylerbwong.stack.ui.drafts

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import me.tylerbwong.markdown.compose.MarkdownText
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.theme.ThemeManager.isNightModeEnabled

@OptIn(ExperimentalLazyDsl::class)
@Composable
fun Drafts() {
    val viewModel = viewModel<DraftsViewModel>()
    val answerDrafts by viewModel.answerDrafts.observeAsState(initial = emptyList())

    LazyColumn {
        item {
            HeaderItem(
                title = stringResource(R.string.drafts),
                subtitle = if (answerDrafts.isEmpty()) {
                    stringResource(R.string.nothing_here)
                } else {
                    null
                }
            )
        }
        items(answerDrafts) { DraftItem(it) }
    }
}

@Composable
private fun HeaderItem(title: String, subtitle: String? = null) {
    Column(
        modifier = Modifier
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp,
            ),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h4.copy(
                color = colorResource(R.color.primaryTextColor),
                fontWeight = FontWeight.Bold,
            ),
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.body1.copy(
                    color = colorResource(R.color.primaryTextColor)
                ),
            )
        }
    }
}

@Composable
private fun DraftItem(item: AnswerDraft) {
    val context = ContextAmbient.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = if (context.isNightModeEnabled) {
                    RippleIndication(color = Color.White)
                } else {
                    RippleIndication()
                }
            ) {
                val intent = QuestionDetailActivity.makeIntent(
                    context = context,
                    id = item.questionId,
                    isInAnswerMode = true
                )
                context.startActivity(intent)
            }
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp,
            ),
    ) {
        Text(
            text = item.questionTitle,
            style = MaterialTheme.typography.subtitle1.copy(
                color = colorResource(R.color.primaryTextColor),
                fontWeight = FontWeight.Bold,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.last_updated, item.formattedTimestamp),
            style = MaterialTheme.typography.caption.copy(
                color = colorResource(R.color.secondaryTextColor)
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        MarkdownText(
            markdown = item.bodyMarkdown,
            color = colorResource(R.color.secondaryTextColor),
            maxLines = 2,
        )
    }
}
