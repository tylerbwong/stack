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
class DeepLinker @Inject constructor(private val siteStore: SiteStore) {
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
        val site = uri.host
        val path = uri.path ?: return DeepLinkResult.PathNotSupportedError

        return when (ResolvedPath.fromPath(path)) {
            AUTH -> DeepLinkResult.RequestingAuth
            QUESTIONS_BY_TAG -> {
                siteStore.deepLinkSite = site
                DeepLinkResult.Success(
                    QuestionsActivity.makeIntentForKey(
                        context,
                        TAGS,
                        uri.lastPathSegment ?: ""
                    )
                )
            }
            QUESTION_DETAILS -> {
                siteStore.deepLinkSite = site
                // Format is /questions/{id}/title so get the second segment
                val id = uri.pathSegments.getOrNull(1)?.toIntOrNull()
                    ?: return DeepLinkResult.PathNotSupportedError
                DeepLinkResult.Success(QuestionDetailActivity.makeIntent(context, id))
            }
            else -> DeepLinkResult.PathNotSupportedError
        }
    }
}
