package me.tylerbwong.stack.ui.utils.markdown

import io.noties.markwon.image.destination.ImageDestinationProcessor
import me.tylerbwong.stack.ui.utils.withHttps
import javax.inject.Inject

class CustomUrlProcessor @Inject constructor() : ImageDestinationProcessor() {
    override fun process(destination: String): String = destination.withHttps
}
