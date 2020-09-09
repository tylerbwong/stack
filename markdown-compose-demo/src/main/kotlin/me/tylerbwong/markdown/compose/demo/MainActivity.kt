@file:Suppress("MagicNumber", "MaxLineLength")

package me.tylerbwong.markdown.compose.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import me.tylerbwong.markdown.compose.MarkdownText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrollableColumn {
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
                        
                        Inline [links](https://google.com) are now also supported! Here is an example of
                        [another link](https://tylerbwong.me).
                        
                        ##### Unordered lists
                        
                        * This is an `unordered` list item
                        - This is another **unordered** list item
                        * This is yet another [unordered](https://tylerbwong.me) list item
                        
                        ##### Ordered lists
                        
                        1. This is an `ordered` list item
                        2. This is another **ordered** list item
                        3) This is yet another [ordered](https://tylerbwong.me) list item
                        
                        ```
                        fun main() {
                            println("I am a code block!")
                        }
                        ```
                    """.trimIndent(),
                    modifier = Modifier.padding(16.dp),
                    onLinkClicked = { link -> Toast.makeText(this@MainActivity, "Link clicked: $link", Toast.LENGTH_LONG).show() }
                )
            }
        }
    }
}
