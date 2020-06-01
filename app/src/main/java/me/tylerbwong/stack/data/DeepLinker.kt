package me.tylerbwong.stack.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import me.tylerbwong.stack.data.DeepLinker.ResolvedPath.AUTH
import me.tylerbwong.stack.data.DeepLinker.ResolvedPath.QUESTIONS_BY_TAG
import me.tylerbwong.stack.data.DeepLinker.ResolvedPath.QUESTION_DETAILS
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.utils.replaceAll
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.questions.QuestionPage.TAGS
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity

class DeepLinker(
    private val authStore: AuthStore,
    private val siteStore: SiteStore
) {
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

    fun resolvePath(context: Context, uri: Uri): Intent? {
        val host = uri.host
        val site = (knownHosts.firstOrNull { it == host }?.replace(".com", "")
            ?: host?.replaceAll(knownHosts, ""))?.removeSuffix(".")

        val path = uri.path ?: return null

        return when (ResolvedPath.fromPath(path)) {
            AUTH -> {
                // When coming back from an auth redirect, save the access token in the hash
                // and restart MainActivity + clear top
                authStore.setAccessToken(uri)
                MainActivity.makeIntentClearTop(context)
            }
            QUESTIONS_BY_TAG -> if (site == siteStore.site) {
                // Format is /questions/tagged/{tag} so use the last segment
                QuestionsActivity.makeIntentForKey(
                    context,
                    TAGS,
                    uri.lastPathSegment ?: "",
                    site
                )
            } else {
                null
            }
            QUESTION_DETAILS -> {
                // Format is /questions/{id}/title so get the second segment
                val id = uri.pathSegments.getOrNull(1)?.toIntOrNull() ?: return null
                QuestionDetailActivity.makeIntent(context, id, site)
            }
            else -> null
        }
    }

    companion object {
        private val knownHosts = listOf(
            "stackoverflow.com",
            "serverfault.com",
            "superuser.com",
            "stackexchange.com"
        )
    }
}
