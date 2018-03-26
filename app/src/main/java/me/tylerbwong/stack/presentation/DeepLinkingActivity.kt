package me.tylerbwong.stack.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.tylerbwong.stack.presentation.questions.detail.QuestionDetailActivity

class DeepLinkingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tokens = intent.data.encodedPath.split('/')
        QuestionDetailActivity.startActivity(this, tokens[2].toInt(), isFromDeepLink = true)
        finish()
    }
}
