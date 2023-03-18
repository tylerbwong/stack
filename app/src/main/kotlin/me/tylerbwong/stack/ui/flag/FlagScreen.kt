package me.tylerbwong.stack.ui.flag

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.FlagOption
import me.tylerbwong.stack.ui.utils.compose.StackTheme
import me.tylerbwong.stack.ui.utils.ofType
import me.tylerbwong.stack.ui.utils.toHtml

private const val MINIMUM_COMMENT_LENGTH = 10

// TODO Fix extreme spaghetti, quick implementation for UGC Policy
@Suppress("LongMethod", "ComplexMethod")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FlagScreen(viewModel: FlagViewModel = viewModel()) {
    val context = LocalContext.current
    val backPressedDispatcher = LocalOnBackPressedDispatcherOwner.current
    val startFlagOptions by viewModel.flagOptions.observeAsState(initial = emptyList())
    var currentFlagOptions by remember { mutableStateOf<List<FlagOption>?>(null) }
    var previousFlagOptions by remember { mutableStateOf<List<List<FlagOption>>>(emptyList()) }
    val title by remember {
        derivedStateOf {
            (currentFlagOptions ?: startFlagOptions).firstOrNull()?.dialogTitle
                ?: context.getString(
                    when (viewModel.postType) {
                        is FlagViewModel.FlagPostType.Question -> R.string.flag_question
                        is FlagViewModel.FlagPostType.Answer -> R.string.flag_answer
                        is FlagViewModel.FlagPostType.Comment -> R.string.flag_comment
                        else -> R.string.flag_post
                    }
                )
        }
    }
    var selectedOption by remember { mutableStateOf<FlagOption?>(null) }
    val nextPage by remember {
        derivedStateOf { viewModel.getNextPageForOption(selectedOption) }
    }
    val isLoading by viewModel.refreshing.observeAsState(initial = false)
    var isCommenting by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    BackHandler {
        if ((currentFlagOptions == null || currentFlagOptions == startFlagOptions) && !isCommenting) {
            context.ofType<Activity>()?.finish()
        } else if (isCommenting) {
            isCommenting = false
            viewModel.extraComment = null
        } else {
            currentFlagOptions = previousFlagOptions.lastOrNull()?.also {
                previousFlagOptions = previousFlagOptions.dropLast(1)
            }
            selectedOption = null
        }
    }
    StackTheme {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .navigationBarsPadding()
                .imePadding(),
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = { context.ofType<Activity>()?.finish() }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = null,
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Button(
                        onClick = { backPressedDispatcher?.onBackPressedDispatcher?.onBackPressed() },
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(
                            text = stringResource(
                                if ((currentFlagOptions == null ||
                                            currentFlagOptions == startFlagOptions) && !isCommenting
                                ) {
                                    R.string.exit
                                } else {
                                    R.string.back
                                }
                            )
                        )
                    }
                    Button(
                        onClick = {
                            if (nextPage == null || (isCommenting && (viewModel.extraComment?.length
                                    ?: 0) >= MINIMUM_COMMENT_LENGTH)
                            ) {
                                viewModel.addFlag(option = selectedOption)
                            } else {
                                when (nextPage) {
                                    FlagPage.Options -> {
                                        currentFlagOptions?.let {
                                            previousFlagOptions = previousFlagOptions + listOf(it)
                                        }
                                        currentFlagOptions =
                                            selectedOption?.subOptions ?: currentFlagOptions
                                        selectedOption = null
                                    }
                                    FlagPage.Comment -> isCommenting = true
                                    FlagPage.Duplicate -> {
                                        currentFlagOptions?.let {
                                            previousFlagOptions = previousFlagOptions + listOf(it)
                                        }
                                        currentFlagOptions =
                                            selectedOption?.subOptions ?: currentFlagOptions
                                        selectedOption = null
                                    }
                                    else -> viewModel.addFlag(option = selectedOption)
                                }
                            }
                        },
                        modifier = Modifier.padding(16.dp),
                        enabled = (selectedOption != null && !isCommenting)
                                || (isCommenting && (viewModel.extraComment?.length
                            ?: 0) >= MINIMUM_COMMENT_LENGTH),
                    ) {
                        Text(
                            text = stringResource(
                                if (nextPage != null && !isCommenting) {
                                    R.string.next
                                } else {
                                    R.string.flag
                                }
                            )
                        )
                    }
                }
            }
        ) {
            AnimatedVisibility(
                visible = !isCommenting,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                LazyColumn(
                    modifier = Modifier.padding(it),
                    verticalArrangement = spacedBy(16.dp),
                ) {
                    if (isLoading) {
                        stickyHeader {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                    }
                    item {
                        Text(
                            text = title,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    itemsIndexed(
                        items = (currentFlagOptions ?: startFlagOptions)
                            .filter { option ->
                                // TODO Support requires question id and requires site
                                option.requiresQuestionId != true && option.requiresSite != true
                            },
                    ) { index, option ->
                        OutlinedCard(
                            onClick = { selectedOption = option },
                            modifier = Modifier
                                .padding(horizontal = 16.dp),
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = selectedOption == option,
                                    onClick = null,
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .fillMaxWidth(),
                                ) {
                                    option.title
                                        ?.capitalize(Locale.current)
                                        ?.toHtml()
                                        ?.toString()
                                        ?.let { optionTitle ->
                                            Text(
                                                text = optionTitle,
                                                style = MaterialTheme.typography.titleMedium,
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                        }
                                    Text(
                                        text = option.description?.toHtml()?.toString() ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                                if (index == (currentFlagOptions ?: startFlagOptions).lastIndex) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = isCommenting,
                enter = fadeIn() + slideInHorizontally { fullWidth -> fullWidth / 2 },
                exit = slideOutHorizontally { fullWidth -> fullWidth / 2 } + fadeOut(),
            ) {
                Box(modifier = Modifier.padding(it)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        AnimatedVisibility(visible = isLoading) {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = title,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.headlineSmall,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = selectedOption?.description?.toHtml()?.toString() ?: "",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            OutlinedTextField(
                                value = viewModel.extraComment ?: "",
                                onValueChange = { viewModel.extraComment = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(IntrinsicSize.Max),
                                placeholder = {
                                    Text(
                                        text = stringResource(
                                            R.string.flag_comment_placeholder,
                                            MINIMUM_COMMENT_LENGTH,
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
