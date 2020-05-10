package me.tylerbwong.stack.ui.utils.markdown

import android.net.Uri
import android.view.View
import io.noties.markwon.LinkResolver
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.ui.utils.launchCustomTab
import timber.log.Timber
import javax.inject.Inject

class CustomTabsLinkResolver @Inject constructor(private val deepLinker: DeepLinker) : LinkResolver {
    override fun resolve(view: View, link: String) {
        val context = view.context
        val deepLinkIntent = deepLinker.resolvePath(context, Uri.parse(link))

        if (deepLinkIntent != null) {
            Timber.i("Resolving internal deep link for $link")
            context.startActivity(deepLinkIntent)
        } else {
            launchCustomTab(context, link)
        }
    }
}
