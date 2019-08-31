package me.tylerbwong.stack.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import me.tylerbwong.stack.data.DeepLinker.ResolvedPath.QUESTIONS_BY_TAG
import me.tylerbwong.stack.data.DeepLinker.ResolvedPath.QUESTION_DETAILS
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity

object DeepLinker {

    private enum class ResolvedPath(vararg val paths: String) {
        QUESTIONS_BY_TAG("/questions/tagged/"),
        QUESTION_DETAILS("/q/", "/questions/");

        companion object {
            fun fromPath(path: String) = values().firstOrNull { resolvedPath ->
                resolvedPath.paths.any { path.contains(it) }
            }
        }
    }

    fun resolvePath(context: Context, uri: Uri): Intent? {
        val path = uri.path ?: return null

        return when (ResolvedPath.fromPath(path)) {
            QUESTIONS_BY_TAG -> {
                // Format is /questions/tagged/{tag} so use the last segment
                QuestionsActivity.makeIntentForKey(context, TAGS, uri.lastPathSegment ?: "")
            }
            QUESTION_DETAILS -> {
                // Format is /questions/{id}/title so get the second segment
                val id = uri.pathSegments.getOrNull(1)?.toIntOrNull() ?: return null
                QuestionDetailActivity.makeIntent(context, id)
            }
            else -> null
        }
    }
}
