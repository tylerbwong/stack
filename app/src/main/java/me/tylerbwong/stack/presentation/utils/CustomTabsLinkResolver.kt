package me.tylerbwong.stack.presentation.utils

import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.view.View
import me.tylerbwong.stack.R
import ru.noties.markwon.spans.LinkSpan

class CustomTabsLinkResolver : LinkSpan.Resolver {
    override fun resolve(view: View?, link: String) {
        val customTabsIntent = CustomTabsIntent.Builder()
                .setToolbarColor(ContextCompat.getColor(
                        view?.context ?: throw IllegalArgumentException("Context cannot be null"),
                        R.color.colorPrimary
                ))
                .build()
        customTabsIntent.launchUrl(view?.context, Uri.parse(link))
    }
}