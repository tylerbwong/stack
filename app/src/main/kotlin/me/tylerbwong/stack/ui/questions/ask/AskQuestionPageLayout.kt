package me.tylerbwong.stack.ui.questions.ask

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.flowlayout.FlowRow
import me.tylerbwong.stack.latex.LatexTextView
import me.tylerbwong.stack.ui.utils.compose.LabeledCheckbox

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
    trailing: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
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

private const val TITLE_LENGTH_LIMIT = 150

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TitleLayoutPreview() {
    var titleText by remember { mutableStateOf(TextFieldValue("")) }
    AskQuestionDetailsLayout(
        title = "Title",
        description = "Be specific and imagine you're asking a question to another person.",
    ) {
        OutlinedTextField(
            value = titleText,
            onValueChange = {
                if (it.text.length <= TITLE_LENGTH_LIMIT) {
                    titleText = it
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = "Summarize the problem here") },
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "${titleText.text.length} / $TITLE_LENGTH_LIMIT",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

private const val MIN_DETAILS_LENGTH = 20

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DetailsLayoutPreview() {
    var detailsText by remember { mutableStateOf(TextFieldValue("")) }
    AskQuestionDetailsLayout(
        title = "What are the details of your problem?",
        description = "Introduce the problem and expand on what you put in the title. Minimum $MIN_DETAILS_LENGTH characters.",
    ) {
        OutlinedTextField(
            value = detailsText,
            onValueChange = { detailsText = it },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            placeholder = { Text(text = "Explain how you encountered the problem") },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ExpandLayoutPreview() {
    var detailsText by remember { mutableStateOf(TextFieldValue("")) }
    AskQuestionDetailsLayout(
        title = "What did you try?",
        description = "Describe what you tried, what you expected, and what actually resulted. Minimum $MIN_DETAILS_LENGTH characters.",
    ) {
        OutlinedTextField(
            value = detailsText,
            onValueChange = { detailsText = it },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            placeholder = { Text(text = "Expand on the problem") },
        )
    }
}

private const val MAX_NUM_TAGS = 5

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TagsLayoutPreview() {
    val tags by remember { mutableStateOf(listOf("android", "kotlin", "jetpack-compose", "java", "chips")) }
    AskQuestionDetailsLayout(
        title = "Tags",
        description = "Add up to $MAX_NUM_TAGS tags to describe what your question is about.",
    ) {
        FlowRow(
            mainAxisSpacing = 8.dp,
        ) {
            tags.forEach {
                InputChip(
                    selected = false,
                    onClick = { /*TODO*/ },
                    label = { Text(text = it) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(InputChipDefaults.IconSize),
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {  },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = "Start typing to see suggestions") },
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            repeat(4) {
                item {
                    TagItem(tag = "javascript", description = "A language that pretty much let's anything fly.")
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DuplicateQuestionLayoutPreview() {
    var isConfirmed by remember { mutableStateOf(false) }
    AskQuestionDetailsLayout(
        title = "Review existing questions",
        description = "Click a post to review if your question is a duplicate.",
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            repeat(5) {
                item {
                    QuestionItem(title = "What does `git pull` do?")
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LabeledCheckbox(
            label = "I confirm that none of these existing posts answers my question.",
            checked = isConfirmed,
            onCheckedChange = { isConfirmed = it },
        )
    }
}

@Composable
fun QuestionItem(title: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun TagItem(tag: String, description: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = tag,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelLarge,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MarkdownText(markdown: String) {
    AndroidView(
        factory = { LatexTextView(it) },
        update = { it.setLatex(markdown) },
    )
}
