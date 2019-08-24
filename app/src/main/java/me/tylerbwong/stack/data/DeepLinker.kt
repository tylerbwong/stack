package me.tylerbwong.stack.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.questions.tags.SingleTagQuestionsActivity

object DeepLinker {

    fun resolvePath(context: Context, uri: Uri): Intent? {
        val path = uri.path ?: return null

        return when {
            path.contains("/questions/tagged/") -> {
                SingleTagQuestionsActivity.makeIntent(context, uri.lastPathSegment ?: "")
            }
            path.contains("/questions/") || path.contains("/q/") -> {
                val id = uri.pathSegments.getOrNull(1)?.toIntOrNull() ?: return null
                QuestionDetailActivity.makeIntent(context, id)
            }
            else -> null
        }
    }
}