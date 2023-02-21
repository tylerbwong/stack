package me.tylerbwong.stack.play.reviewer

import android.app.Activity
import android.content.SharedPreferences
import com.google.android.play.core.review.ReviewManager
import me.tylerbwong.stack.data.reviewer.AppReviewer

class PlayAppReviewer(
    private val manager: ReviewManager,
    private val preferences: SharedPreferences,
) : AppReviewer {
    override fun initializeReviewFlow(
        activity: Activity,
        onReviewComplete: () -> Unit,
        onReviewRequestError: (Exception) -> Unit,
    ) {
        if (preferences.contains(SCREEN_MARKER)) {
            val request = manager.requestReviewFlow()
            request.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val reviewInfo = task.result
                    val flow = manager.launchReviewFlow(activity, reviewInfo)
                    flow.addOnCompleteListener { onReviewComplete() }
                } else {
                    task.exception?.let(onReviewRequestError)
                }
            }
        }
    }

    override fun markReviewImpression() {
        if (!preferences.contains(SCREEN_MARKER)) {
            preferences.edit()
                .putBoolean(SCREEN_MARKER, true)
                .apply()
        }
    }

    companion object {
        private const val SCREEN_MARKER = "screen_marker"
    }
}
