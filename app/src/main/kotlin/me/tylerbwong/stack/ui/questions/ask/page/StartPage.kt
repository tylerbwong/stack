package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.delay
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.utils.compose.MarkdownText
import me.tylerbwong.stack.ui.utils.compose.StackTheme
import me.tylerbwong.stack.ui.utils.toHtml

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
    LaunchedEffect(currentSite) {
        if (viewModel.shouldSaveDraft) {
            delay(1_000)
            viewModel.saveDraft()
        }
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
