package me.tylerbwong.stack.ui.questions

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.viewModel
import me.tylerbwong.stack.R
import java.util.Locale

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun QuestionsScreen(title: String, onBackPressed: () -> Unit) {
    val viewBackgroundColor = colorResource(R.color.viewBackgroundColor)
    val primaryTextColor = colorResource(R.color.primaryTextColor)
    val iconColor = colorResource(R.color.iconColor)

    val viewModel = viewModel<QuestionsViewModel>()
    var isDropdownMenuVisible by savedInstanceState { false }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = primaryTextColor,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        icon = {
                            Icon(
                                asset = Icons.Filled.ArrowBack,
                                tint = iconColor,
                            )
                        }
                    )
                },
                actions = {
                    // TODO Remove when SwipeRefresh is supported
                    IconButton(
                        onClick = { viewModel.getQuestions() },
                        icon = {
                            Icon(
                                asset = Icons.Filled.Refresh,
                                tint = iconColor
                            )
                        }
                    )
                    DropdownMenu(
                        toggle = {
                            IconButton(
                                onClick = { isDropdownMenuVisible = true },
                                icon = {
                                    Icon(
                                        asset = Icons.Filled.Sort,
                                        tint = iconColor
                                    )
                                }
                            )
                        },
                        expanded = isDropdownMenuVisible,
                        onDismissRequest = { isDropdownMenuVisible = false },
                        dropdownModifier = Modifier
                            .fillMaxWidth(fraction = 0.5f)
                            .background(colorResource(R.color.dialogBackgroundColor)),
                    ) {
                        listOf(
                            stringResource(R.string.creation),
                            stringResource(R.string.activity),
                            stringResource(R.string.votes),
                            stringResource(R.string.hot),
                            stringResource(R.string.week),
                            stringResource(R.string.month),
                        ).forEach { sort ->
                            DropdownMenuItem(
                                onClick = {
                                    viewModel.getQuestions(
                                        sort = sort.toLowerCase(Locale.getDefault())
                                    )
                                    isDropdownMenuVisible = false
                                },
                            ) {
                                Text(
                                    text = sort,
                                    color = primaryTextColor,
                                )
                            }
                        }
                    }
                },
                backgroundColor = viewBackgroundColor,
            )
        },
        backgroundColor = viewBackgroundColor,
    ) {
        val questions by viewModel.data.observeAsState(initial = emptyList())
        LazyColumnFor(items = questions) { QuestionItem(question = it) }
    }
}
