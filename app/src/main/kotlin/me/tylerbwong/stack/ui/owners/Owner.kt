package me.tylerbwong.stack.ui.owners

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import dev.chrisbanes.accompanist.coil.CoilImage
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.BadgeCounts
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.utils.colorAttribute
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.toHtml

@Composable
fun Owner(
    owner: User,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val context = ContextAmbient.current
        CoilImage(
            request = ImageRequest.Builder(ContextAmbient.current)
                .data(owner.profileImage)
                .error(R.drawable.user_image_placeholder)
                .placeholder(R.drawable.user_image_placeholder)
                .transformations(CircleCropTransformation())
                .build(),
            modifier = Modifier
                .width(28.dp)
                .height(28.dp)
                .clickable(
                    onClick = { ProfileActivity.startActivity(context, userId = owner.userId) },
                    indication = null,
                ),
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,
            fadeIn = true,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = owner.displayName.toHtml().toString(),
                color = colorAttribute(android.R.attr.textColorPrimary),
                fontSize = 13.sp,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = owner.reputation.toLong().format(),
                    color = colorAttribute(android.R.attr.textColorSecondary),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
                owner.badgeCounts?.let {
                    Spacer(modifier = Modifier.width(8.dp))
                    Badges(badgeCounts = it)
                }
            }
        }
    }
}

@Preview
@Composable
fun OwnerPreview() {
    Owner(
        owner = User(
            aboutMe = null,
            acceptRate = null,
            accountId = null,
            displayName = "Tyler Wong",
            link = null,
            location = null,
            profileImage = "https://www.gravatar.com/avatar/064c83de9d5058758e478cfc6ac4dbef?s=128&d=identicon&r=PG&f=1",
            reputation = 30000,
            userId = 0,
            userType = "",
            badgeCounts = BadgeCounts(32, 2, 1),
        )
    )
}
