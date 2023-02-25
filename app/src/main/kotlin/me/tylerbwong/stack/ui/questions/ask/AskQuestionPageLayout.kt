package me.tylerbwong.stack.ui.questions.ask

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.delay
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.latex.LatexTextView
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.compose.StackTheme
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.toHtml

private const val TITLE_LENGTH_LIMIT = 150
private const val MIN_DETAILS_LENGTH = 20
private const val MAX_NUM_TAGS = 5

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartPage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    val context = LocalContext.current
    val currentSite by viewModel.currentSite.observeAsState()
    val sites by viewModel.getSites().collectAsState(initial = emptyList())
    LaunchedEffect(key1 = null) {
        viewModel.fetchSite()
    }
    AskQuestionDetailsLayout(
        title = "Ask a public question",
        description = "Posting a question to:"
    ) {
        if (currentSite != null) {
            var isExpanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.height(IntrinsicSize.Min)) {
                OutlinedTextField(
                    value = currentSite?.name ?: "",
                    enabled = true,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(currentSite?.iconUrl)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(36.dp)
                                .padding(start = 4.dp),
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (isExpanded) {
                                Icons.Filled.ArrowDropUp
                            } else {
                                Icons.Filled.ArrowDropDown
                            },
                            contentDescription = null,
                        )
                    },
                    onValueChange = {},
                )

                // Transparent clickable surface on top of OutlinedTextField
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.extraSmall)
                        .clickable { isExpanded = true },
                    color = Color.Transparent,
                ) {}
            }

            if (isExpanded) {
                Dialog(onDismissRequest = { isExpanded = false }) {
                    StackTheme {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            val listState = rememberLazyListState()
                            val selectedIndex by remember {
                                derivedStateOf {
                                    sites.indexOfFirst { it.name == currentSite?.name }
                                }
                            }
                            if (selectedIndex > -1) {
                                LaunchedEffect(null) {
                                    listState.scrollToItem(index = selectedIndex)
                                }
                            }

                            LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                                itemsIndexed(sites) { index, item ->
                                    val isSelected = index == selectedIndex
                                    val contentColor = when {
                                        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = MaterialColors.ALPHA_FULL)
                                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = MaterialColors.ALPHA_FULL)
                                    }

                                    CompositionLocalProvider(LocalContentColor provides contentColor) {
                                        Row(
                                            modifier = Modifier
                                                .clickable {
                                                    viewModel.setCurrentSite(item.parameter)
                                                    isExpanded = false
                                                }
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(item.iconUrl)
                                                    .build(),
                                                contentDescription = null,
                                                modifier = Modifier.size(36.dp),
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Text(
                                                text = item.name.toHtml().toString(),
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        MarkdownText(
            markdown = """
                ### Steps
                * Summarize your problem in a one-line title.  
                * Describe your problem in more detail.  
                * Describe what you tried and what you expected to happen.
                * Add "tags" which help surface your question to members of the community.
                * Review your question and post it to the site.
            """.trimIndent(),
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitlePage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    var titleText by remember { mutableStateOf(TextFieldValue(viewModel.title.value ?: "")) }
    AskQuestionDetailsLayout(
        title = "Title",
        description = "Be specific and imagine you're asking a question to another person.",
    ) {
        OutlinedTextField(
            value = titleText,
            onValueChange = {
                if (it.text.length <= TITLE_LENGTH_LIMIT) {
                    titleText = it
                    viewModel.setTitle(it.text)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsPage() {
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
@Composable
fun ExpandDetailsPage() {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsPage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    var searchQuery by remember { mutableStateOf("") }
    LaunchedEffect(key1 = searchQuery) {
        delay(500)
        viewModel.fetchPopularTags(searchQuery)
    }
    val searchTags by viewModel.tags.observeAsState(initial = emptyList())
    val isSearchTagsVisible by remember(searchTags) {
        derivedStateOf { searchTags.isNotEmpty() }
    }
    val selectedTags by viewModel.selectedTags.observeAsState(initial = emptySet())
    val isSelectedTagsVisible by remember(selectedTags) {
        derivedStateOf { selectedTags.isNotEmpty() }
    }
    AskQuestionDetailsLayout(
        title = "Tags",
        description = "Add up to $MAX_NUM_TAGS tags to describe what your question is about.",
    ) {
        AnimatedVisibility(visible = isSelectedTagsVisible) {
            Spacer(modifier = Modifier.height(32.dp))
            FlowRow(mainAxisSpacing = 8.dp) {
                selectedTags.forEach {
                    ElevatedFilterChip(
                        selected = true,
                        onClick = { viewModel.setSelectedTags(selectedTags - it) },
                        label = { Text(text = it.name) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(InputChipDefaults.IconSize),
                            )
                        },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = { Text(text = "Start typing to see suggestions") },
            singleLine = true,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(visible = isSearchTagsVisible) {
            val scrollState = rememberScrollState()
            Spacer(modifier = Modifier.height(64.dp))
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState),
                mainAxisSpacing = 8.dp,
            ) {
                searchTags.forEach {
                    InputChip(
                        selected = false,
                        onClick = { viewModel.setSelectedTags(selectedTags + it) },
                        label = { Text(text = it.name) },
                    )
                }
            }
        }
    }
}

@Composable
fun DuplicateQuestionPage() {
    val context = LocalContext.current
    val viewModel = viewModel<AskQuestionViewModel>()
    val title by viewModel.title.observeAsState()
    val selectedTags by viewModel.selectedTags.observeAsState()
    val searchSimilar by remember { derivedStateOf { title + selectedTags } }
    LaunchedEffect(searchSimilar) {
        viewModel.searchSimilar()
    }
    val similarQuestions by viewModel.similarQuestions.observeAsState(initial = emptyList())
    AskQuestionDetailsLayout(
        title = "Review existing questions",
        description = "Click a post to review if your question is a duplicate.",
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(similarQuestions) { index, item ->
                QuestionItem(question = item) {
                    QuestionDetailActivity.startActivity(context, item.questionId)
                }
                if (index < similarQuestions.lastIndex) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ReviewPage() {

}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionItem(question: Question, onClick: () -> Unit) {
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

@Composable
private fun MarkdownText(
    markdown: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    val textSize = if (fontSize != TextUnit.Unspecified) {
        with(LocalDensity.current) {
            fontSize.toDp().value
        }
    } else {
        0f
    }
    AndroidView(
        factory = {
            LatexTextView(it).apply {
                if (fontSize != TextUnit.Unspecified) {
                    this.textSize = textSize
                }
            }
        },
        modifier = modifier,
        update = { it.setLatex(markdown) },
    )
}
