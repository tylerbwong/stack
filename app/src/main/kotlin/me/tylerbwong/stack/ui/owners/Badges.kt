package me.tylerbwong.stack.ui.owners

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.BadgeCounts

@Composable
fun Badges(
    badgeCounts: BadgeCounts,
    labelColor: Color = colorResource(R.color.colorTextPrimary),
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val (bronze, silver, gold) = badgeCounts
        val countColorMapping = listOf(
            gold to colorResource(R.color.goldBadgeColor),
            silver to colorResource(R.color.silverBadgeColor),
            bronze to colorResource(R.color.bronzeBadgeColor),
        ).filter { (count, _) -> count > 0 }
        countColorMapping.forEachIndexed { index, (count, color) ->
            Badge(color)
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = count.toString(),
                color = labelColor,
                fontSize = 12.sp,
                lineHeight = 12.sp,
            )
            if (index != countColorMapping.lastIndex) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
fun Badge(color: Color) {
    Canvas(
        modifier = Modifier.size(7.dp),
        onDraw = { drawCircle(color) }
    )
}

@Preview
@Composable
fun BadgesPreview() {
    Badges(badgeCounts = BadgeCounts(32, 2, 1))
}
