package me.tylerbwong.stack.ui.questions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.ui.owners.Owner
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.theme.ThemeManager.isNightModeEnabled
import me.tylerbwong.stack.ui.utils.colorAttribute
import me.tylerbwong.stack.ui.utils.systemService
import me.tylerbwong.stack.ui.utils.toHtml

private const val LABEL = "linkText"

@Composable
fun QuestionItem(question: Question) {
    val context = ContextAmbient.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = if (ContextAmbient.current.isNightModeEnabled) {
                    RippleIndication(color = Color.White)
                } else {
                    RippleIndication()
                },
                onClick = { QuestionDetailActivity.startActivity(context, question.questionId) },
                onLongClick = {
                    context.systemService<ClipboardManager>(Context.CLIPBOARD_SERVICE)?.let {
                        it.setPrimaryClip(ClipData.newPlainText(LABEL, question.shareLink))
                        Toast.makeText(context, R.string.link_copied, Toast.LENGTH_SHORT).show()
                    }
                }
            )
            .padding(16.dp),
    ) {
        Text(
            text = question.title.toHtml().toString(),
            style = MaterialTheme.typography.h6.copy(
                color = colorAttribute(android.R.attr.textColorPrimary),
                fontSize = 16.sp,
            ),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Owner(owner = question.owner)
//            Text(
//                text = question.answerCount.toLong().format(),
//                color = colorResource(R.color.secondaryTextColor),
//            )
        }
    }
}
