package me.tylerbwong.stack.ui.utils.markdown

import android.view.View
import me.tylerbwong.stack.ui.utils.launchCustomTab
import ru.noties.markwon.core.spans.LinkSpan

class CustomTabsLinkResolver : LinkSpan.Resolver {
    override fun resolve(view: View, link: String) = launchCustomTab(view.context, link)
}
