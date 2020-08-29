package me.tylerbwong.stack.ui.utils.markdown

import androidx.compose.runtime.Composable
import androidx.ui.tooling.preview.Preview
import me.tylerbwong.markdown.compose.MarkdownText

@Preview
@Composable
fun TestMarkdownText() {
    MarkdownText(
        markdown = """
            # This is an h1 header
            ## This is an h2 header
            ### This is an **h3** header
            #### This is an _h4_ header
            ##### This is an `h5` header
            ###### This is an ~~h6~~ header
            
            This is a **full** on test with some _markdown_!
            What's really cool is the `code snippets` that show up too.
            
            ~~Strikethrough is also supported~~
            
            # _~~**`This is a test with every kind of formatting`**~~_
        """.trimIndent()
    )
}
