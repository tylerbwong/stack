package me.tylerbwong.stack.ui.utils.markdown

import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration

class UrlPlugin private constructor() : AbstractMarkwonPlugin() {
    private val urlProcessor = CustomUrlProcessor()
    private val tabsResolver = CustomTabsLinkResolver()

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder
            .imageDestinationProcessor(urlProcessor)
            .linkResolver(tabsResolver)
    }

    companion object {
        fun create() = UrlPlugin()
    }
}
