package me.tylerbwong.stack.markdown.utils

import org.apache.commons.text.StringEscapeUtils

internal fun String.stripSpecials() = StringEscapeUtils.unescapeHtml4(this)
