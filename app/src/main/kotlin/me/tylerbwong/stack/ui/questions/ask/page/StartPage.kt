package me.tylerbwong.stack.ui.questions.ask.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import me.tylerbwong.stack.ui.questions.ask.AskQuestionViewModel
import me.tylerbwong.stack.ui.settings.sites.SitesActivity
import me.tylerbwong.stack.ui.utils.compose.MarkdownText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartPage() {
    val viewModel = viewModel<AskQuestionViewModel>()
    val context = LocalContext.current
    val currentSiteParameter by viewModel.currentSiteParameter.observeAsState()
    val currentSite by viewModel.currentSite.observeAsState()
    LaunchedEffect(currentSiteParameter) {
        viewModel.fetchSite()
    }
    AskQuestionDetailsLayout(
        title = "Ask a public question",
        description = "Posting a question to:"
    ) {
        if (currentSite != null) {
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
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                        )
                    },
                    onValueChange = {},
                )

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.extraSmall)
                        .clickable { SitesActivity.startActivity(context) },
                    color = Color.Transparent,
                ) {}
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
