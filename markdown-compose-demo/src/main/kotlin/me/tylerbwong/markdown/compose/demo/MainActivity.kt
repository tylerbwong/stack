@file:Suppress("MaxLineLength")
package me.tylerbwong.markdown.compose.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.setContent
import androidx.ui.tooling.preview.Preview
import me.tylerbwong.markdown.compose.MarkdownText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarkdownTextDemo()
        }
    }
}

@Preview
@Composable
fun MarkdownTextDemo() {
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
            
            Inline images are supported as well ![Kotlin](https://developer.android.com/images/jetpack/info-bytes-compose-less-code.png)!
            Reference images are coming soon!
            
        """.trimIndent()
    )
}
