package me.tylerbwong.stack.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity

class DeepLinkingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.data?.encodedPath?.split('/')?.let {
            QuestionDetailActivity.startActivity(this, it[2].toInt(), isFromDeepLink = true)
        }
        finish()
    }
}
