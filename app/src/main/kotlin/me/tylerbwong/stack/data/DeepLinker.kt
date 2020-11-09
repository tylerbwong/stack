package me.tylerbwong.stack.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import me.tylerbwong.stack.data.DeepLinker.ResolvedPath.AUTH
import me.tylerbwong.stack.data.DeepLinker.ResolvedPath.QUESTIONS_BY_TAG
import me.tylerbwong.stack.data.DeepLinker.ResolvedPath.QUESTION_DETAILS
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import javax.inject.Inject
import javax.inject.Singleton

sealed class DeepLinkResult {
    class Success(val intent: Intent) : DeepLinkResult()
    object RequestingAuth : DeepLinkResult()
    object PathNotSupportedError : DeepLinkResult()
}

@Singleton
class DeepLinker @Inject constructor() {
    private enum class ResolvedPath(vararg val paths: String) {
        AUTH("/auth/redirect"),
        QUESTIONS_BY_TAG("/questions/tagged/"),
        QUESTION_DETAILS("/q/", "/questions/");

        companion object {
            fun fromPath(path: String) = values().firstOrNull { resolvedPath ->
                resolvedPath.paths.any { path.contains(it) }
            }
        }
    }

    fun resolvePath(context: Context, uri: Uri): DeepLinkResult {
        val site = uri.host?.normalizeSite()
        val path = uri.path ?: return DeepLinkResult.PathNotSupportedError

        return when (ResolvedPath.fromPath(path)) {
            AUTH -> DeepLinkResult.RequestingAuth
            QUESTIONS_BY_TAG -> {
                DeepLinkResult.Success(
                    QuestionsActivity.makeIntentForKey(
                        context,
                        TAGS,
                        uri.lastPathSegment ?: "",
                        deepLinkSite = site
                    )
                )
            }
            QUESTION_DETAILS -> {
                // Format is /questions/{questionId}/title/{answerId} so get the second segment
                val questionId = uri.pathSegments.getOrNull(1)?.toIntOrNull()
                    ?: return DeepLinkResult.PathNotSupportedError
                val answerId = uri.pathSegments.getOrNull(3)?.toIntOrNull()
                DeepLinkResult.Success(
                    QuestionDetailActivity.makeIntent(
                        context = context,
                        questionId = questionId,
                        answerId = answerId,
                        deepLinkSite = site
                    )
                )
            }
            else -> DeepLinkResult.PathNotSupportedError
        }
    }

    /**
     * In order to extract the correct site parameter to pass around, all non Stack Exchange sites
     * need to drop ".stackexchange.com" and all others need to drop ".com". This is a rudimentary
     * solution and will need to be updated if any site is added that does not end in ".com".
     */
    private fun String.normalizeSite(): String =
        removeSuffix(".com").removeSuffix(".stackexchange")
}
