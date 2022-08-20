package me.tylerbwong.stack.base.reviewer

import android.app.Activity
import me.tylerbwong.stack.data.reviewer.AppReviewer

class NoOpAppReviewer : AppReviewer {
    override fun initializeReviewFlow(
        activity: Activity,
        onReviewComplete: () -> Unit,
        onReviewRequestError: (Exception) -> Unit
    ) {
        // No-op
    }

    override fun markReviewImpression() {
        // No-op
    }
}
