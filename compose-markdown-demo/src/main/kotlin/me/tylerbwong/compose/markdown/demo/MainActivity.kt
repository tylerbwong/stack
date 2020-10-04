@file:Suppress("MagicNumber")
package me.tylerbwong.compose.markdown.demo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.unit.dp
import me.tylerbwong.compose.markdown.MarkdownText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrollableColumn(contentPadding = PaddingValues(16.dp)) {
                MarkdownText(
                    markdown = assets.open("test.md").bufferedReader().use { it.readText() },
                    onLinkClicked = { link ->
                        Toast.makeText(
                            this@MainActivity,
                            "Link clicked: $link",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            }
        }
    }
}
