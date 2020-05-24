package me.tylerbwong.stack.ui.utils.markdown

import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import javax.inject.Inject

class UrlPlugin @Inject constructor(
    private val urlProcessor: CustomUrlProcessor,
    private val tabsResolver: CustomTabsLinkResolver
) : AbstractMarkwonPlugin() {

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder
            .imageDestinationProcessor(urlProcessor)
            .linkResolver(tabsResolver)
    }
}
