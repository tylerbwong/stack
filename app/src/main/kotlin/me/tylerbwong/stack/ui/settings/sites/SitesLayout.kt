@file:Suppress("MagicNumber")
package me.tylerbwong.stack.ui.settings.sites

import android.annotation.SuppressLint
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.RippleIndication
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import dev.chrisbanes.accompanist.coil.CoilImage
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.ui.theme.ThemeManager.isNightModeEnabled
import me.tylerbwong.stack.ui.utils.toHtml

@Composable
fun SitesLayout(
    sitesLiveData: LiveData<List<Site>>,
    onSiteSelected: (Site) -> Unit
) {
    val sites by sitesLiveData.observeAsState(initial = emptyList())
    LazyColumnFor(items = sites) { site -> SiteItem(site, onSiteSelected) }
}

@SuppressLint("DefaultLocale")
@Composable
fun SiteItem(
    site: Site,
    onSiteSelected: (Site) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable(
                indication = if (ContextAmbient.current.isNightModeEnabled) {
                    RippleIndication(color = Color.White)
                } else {
                    RippleIndication()
                }
            ) { onSiteSelected(site) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoilImage(
            data = site.iconUrl,
            modifier = Modifier.width(64.dp).height(64.dp)
                .padding(start = 8.dp, top = 8.dp, end = 4.dp, bottom = 8.dp)
        )
        Column(
            modifier = Modifier.padding(start = 4.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = site.name.toHtml().toString(),
                color = colorResource(R.color.primaryTextColor),
                style = MaterialTheme.typography.body1
            )
            Text(
                text = site.audience.capitalize().toHtml().toString(),
                color = colorResource(R.color.secondaryTextColor),
                style = MaterialTheme.typography.body2
            )
        }
    }
}
