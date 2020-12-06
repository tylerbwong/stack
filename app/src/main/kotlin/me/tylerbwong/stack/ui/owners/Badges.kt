@file:Suppress("MagicNumber")
package me.tylerbwong.stack.ui.owners

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.BadgeCounts

@Composable
fun Badges(
    badgeCounts: BadgeCounts,
    modifier: Modifier = Modifier,
    labelColor: Color = colorResource(R.color.colorTextPrimary),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val (bronze, silver, gold) = badgeCounts
        val countColorMapping = listOf(
            gold to R.color.goldBadgeColor,
            silver to R.color.silverBadgeColor,
            bronze to R.color.bronzeBadgeColor,
        ).filter { (count, _) -> count > 0 }
        countColorMapping.forEachIndexed { index, (count, color) ->
            BadgeCircle(color = colorResource(color))
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = count.toString(),
                color = labelColor,
                fontSize = 12.sp,
            )
            if (index != countColorMapping.lastIndex) {
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
private fun BadgeCircle(color: Color) {
    Canvas(
        modifier = Modifier.size(7.dp),
        onDraw = { drawCircle(color) }
    )
}

@Preview
@Composable
private fun BadgesPreview() {
    Badges(badgeCounts = BadgeCounts(bronze = 32, silver = 2, gold = 1))
}
