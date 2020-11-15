package me.tylerbwong.stack.ui.utils.markdown

import io.noties.markwon.ext.latex.JLatexMathNode
import io.noties.markwon.inlineparser.InlineProcessor
import org.commonmark.node.Node
import java.util.regex.Pattern
import javax.inject.Inject

class LatexInlineProcessor @Inject constructor() : InlineProcessor() {

    override fun specialCharacter(): Char = INLINE_DELIMITER

    override fun parse(): Node? {
        val latex = match(pattern)

        return if (latex == null) {
            null
        } else {
            JLatexMathNode().also {
                it.latex(
                    latex
                        .removePrefix(BLOCK_DELIMITER)
                        .removeSuffix(BLOCK_DELIMITER)
                        .removePrefix(INLINE_DELIMITER.toString())
                        .removeSuffix(INLINE_DELIMITER.toString())
                )
            }
        }
    }

    companion object {
        private const val INLINE_DELIMITER = '$'
        private const val BLOCK_DELIMITER = "$INLINE_DELIMITER$INLINE_DELIMITER"
        private val pattern = Pattern.compile("(\\\$+)([\\s\\S]+?)\\1")
    }
}
