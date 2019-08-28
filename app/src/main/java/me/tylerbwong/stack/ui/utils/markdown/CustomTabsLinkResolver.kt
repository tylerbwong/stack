package me.tylerbwong.stack.ui.utils.markdown

import android.net.Uri
import android.view.View
import io.noties.markwon.LinkResolver
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.ui.utils.launchCustomTab
import timber.log.Timber

class CustomTabsLinkResolver : LinkResolver {
    override fun resolve(view: View, link: String) {
        val context = view.context
        val deepLinkIntent = DeepLinker.resolvePath(context, Uri.parse(link))

        if (deepLinkIntent != null) {
            Timber.i("Resolving internal deep link for $link")
            context.startActivity(deepLinkIntent)
        } else {
            launchCustomTab(context, link)
        }
    }
}
