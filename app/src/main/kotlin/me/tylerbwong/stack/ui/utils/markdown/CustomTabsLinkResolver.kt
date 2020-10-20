package me.tylerbwong.stack.ui.utils.markdown

import android.net.Uri
import android.view.View
import io.noties.markwon.LinkResolver
import me.tylerbwong.stack.data.DeepLinkResult
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.ui.utils.launchUrl
import me.tylerbwong.stack.ui.utils.withHttps
import timber.log.Timber
import javax.inject.Inject

class CustomTabsLinkResolver @Inject constructor(
    private val deepLinker: DeepLinker
) : LinkResolver {
    override fun resolve(view: View, link: String) {
        val newLink = link.withHttps
        val context = view.context
        val deepLinkResult = deepLinker.resolvePath(context, Uri.parse(newLink))

        if (deepLinkResult is DeepLinkResult.Success) {
            Timber.i("Resolving internal deep link for $newLink")
            context.startActivity(deepLinkResult.intent)
        } else {
            context.launchUrl(newLink)
        }
    }
}
