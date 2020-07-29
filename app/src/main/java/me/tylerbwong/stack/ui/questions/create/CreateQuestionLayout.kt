package me.tylerbwong.stack.ui.questions.create

import androidx.compose.Composable
import androidx.ui.foundation.Text
import androidx.ui.layout.Column
import androidx.ui.material.IconButton
import androidx.ui.material.TopAppBar
import androidx.ui.res.stringResource
import androidx.ui.res.vectorResource
import androidx.ui.tooling.preview.Preview
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.theme.StackTheme

@Preview
@Composable
fun CreateQuestionLayout(onBackPressed: () -> Unit = {}) {
    StackTheme {
        Column {
            TopAppBar(
                title = { Text(text = stringResource(R.string.create)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed,
                        icon = { vectorResource(R.drawable.ic_arrow_back) }
                    )
                }
            )
        }
    }
}
