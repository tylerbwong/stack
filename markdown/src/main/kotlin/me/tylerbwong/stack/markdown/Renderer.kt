package me.tylerbwong.stack.markdown

import android.text.Spanned
import org.commonmark.node.Node

interface Renderer {
    fun render(node: Node): Spanned
}
