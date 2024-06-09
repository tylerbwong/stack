package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.questions.ask.page.AskQuestionPage.Companion.MIN_DETAILS_LENGTH

@Composable
fun DetailsPage(isDetailedQuestionRequired: Boolean) {
    val viewModel = viewModel<AskQuestionViewModel>()
    AskQuestionDetailsLayout(
        title = stringResource(
            if (isDetailedQuestionRequired) {
                R.string.details_page_title_detailed
            } else {
                R.string.details_page_title
            }
        ),
        description = stringResource(
            if (isDetailedQuestionRequired) {
                R.string.details_page_description_detailed
            } else {
                R.string.details_page_description
            },
            MIN_DETAILS_LENGTH
        ),
    ) {
        OutlinedTextField(
            value = viewModel.body,
            onValueChange = viewModel::updateBody,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            placeholder = {
                Text(
                    text = stringResource(
                        if (isDetailedQuestionRequired) {
                            R.string.details_page_hint_detailed
                        } else {
                            R.string.details_page_hint
                        }
                    )
                )
            },
        )
    }
}

@Composable
fun ExpandDetailsPage(isDetailedQuestionRequired: Boolean) {
    val viewModel = viewModel<AskQuestionViewModel>()
    AskQuestionDetailsLayout(
        title = stringResource(
            if (isDetailedQuestionRequired) {
                R.string.expand_details_page_title_detailed
            } else {
                R.string.expand_details_page_title
            }
        ),
        description = if (isDetailedQuestionRequired) {
            stringResource(R.string.expand_details_page_description_detailed, MIN_DETAILS_LENGTH)
        } else {
            stringResource(R.string.expand_details_page_description)
        },
    ) {
        OutlinedTextField(
            value = viewModel.expandBody,
            onValueChange = viewModel::updateExpandBody,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            placeholder = {
                Text(
                    text = stringResource(
                        if (isDetailedQuestionRequired) {
                            R.string.expand_details_hint_detailed
                        } else {
                            R.string.expand_details_hint
                        }
                    )
                )
            },
        )
    }
}
