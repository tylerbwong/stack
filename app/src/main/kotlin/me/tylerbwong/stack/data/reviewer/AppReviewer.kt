package me.tylerbwong.stack.data.reviewer

import android.app.Activity

interface AppReviewer {
    fun initializeReviewFlow(
        activity: Activity,
        onReviewComplete: () -> Unit = {},
        onReviewRequestError: (Exception) -> Unit = {},
    )

    fun markReviewImpression()
}
