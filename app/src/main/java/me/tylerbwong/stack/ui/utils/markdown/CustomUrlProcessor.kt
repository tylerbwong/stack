package me.tylerbwong.stack.ui.utils.markdown

import io.noties.markwon.urlprocessor.UrlProcessor

class CustomUrlProcessor : UrlProcessor {
    override fun process(destination: String): String {
        var url = destination
        if (!destination.contains("http")) {
            url = "https:$destination"
        }
        return url
    }
}
