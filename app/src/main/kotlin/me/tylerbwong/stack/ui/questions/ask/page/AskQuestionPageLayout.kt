package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Requirements for create question flow:
 * 1. Should be a multi-step process with a step-progress indicator and back + next, review, and submit buttons
 * 2. Needs to support formatting tools for long body descriptions
 * 3. Tags should show results as typed, and then tag-ified when selected.
 */
@Composable
fun AskQuestionDetailsLayout(
    title: String,
    description: String,
    scrollable: Boolean = true,
    trailing: @Composable () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .then(if (scrollable) Modifier.verticalScroll(rememberScrollState()) else Modifier),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(16.dp))
        trailing()
    }
}
