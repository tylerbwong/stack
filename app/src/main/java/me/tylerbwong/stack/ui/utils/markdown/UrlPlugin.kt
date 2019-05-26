package me.tylerbwong.stack.ui.utils.markdown

import ru.noties.markwon.AbstractMarkwonPlugin
import ru.noties.markwon.MarkwonConfiguration

class UrlPlugin private constructor() : AbstractMarkwonPlugin() {
    private val urlProcessor = CustomUrlProcessor()
    private val tabsResolver = CustomTabsLinkResolver()

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder
                .urlProcessor(urlProcessor)
                .linkResolver(tabsResolver)
    }

    companion object {
        fun create() = UrlPlugin()
    }
}
