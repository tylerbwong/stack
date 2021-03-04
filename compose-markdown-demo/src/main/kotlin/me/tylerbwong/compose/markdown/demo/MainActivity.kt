@file:Suppress("MagicNumber")
package me.tylerbwong.compose.markdown.demo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.unit.dp
import me.tylerbwong.compose.markdown.MarkdownText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                item {
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
}
