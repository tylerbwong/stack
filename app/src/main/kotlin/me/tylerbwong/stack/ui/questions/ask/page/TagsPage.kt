package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.delay
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel

private const val MAX_NUM_TAGS = 5

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
    val isSelectedTagsVisible by remember(viewModel.selectedTags) {
        derivedStateOf { viewModel.selectedTags.isNotEmpty() }
    }
    AskQuestionDetailsLayout(
        title = "Tags",
        description = "Add up to $MAX_NUM_TAGS tags to describe what your question is about.",
    ) {
        AnimatedVisibility(visible = isSelectedTagsVisible) {
            Spacer(modifier = Modifier.height(32.dp))
            FlowRow(mainAxisSpacing = 8.dp) {
                viewModel.selectedTags.forEach {
                    ElevatedFilterChip(
                        selected = true,
                        onClick = { viewModel.selectedTags = viewModel.selectedTags - it },
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
                        onClick = {
                            if (viewModel.selectedTags.size < MAX_NUM_TAGS) {
                                viewModel.selectedTags = viewModel.selectedTags + it
                            }
                        },
                        label = { Text(text = it.name) },
                    )
                }
            }
        }
    }
}
