package me.tylerbwong.stack.api.model

import androidx.annotation.StringDef
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

const val COMMENTED = "commented"
const val ASKED = "asked"
const val ANSWERED = "answered"
const val BADGE = "badge"
const val REVISION = "revision"
const val ACCEPTED = "accepted"
const val REVIEWED = "reviewed"
const val SUGGESTED = "suggested"

@StringDef(COMMENTED, ASKED, ANSWERED, BADGE, REVISION, ACCEPTED, REVIEWED, SUGGESTED)
annotation class TimelineType

const val QUESTION = "question"
const val ANSWER = "answer"

@StringDef(QUESTION, ANSWER)
annotation class PostType

@JsonClass(generateAdapter = true)
data class TimelineEvent(
    @Json(name = "badge_id")
    val badgeId: Int?,
    @Json(name = "comment_id")
    val commentId: Int?,
    @Json(name = "creation_date")
    val creationDate: Long,
    @Json(name = "detail")
    val detail: String?,
    @Json(name = "link")
    val link: String,
    @Json(name = "post_id")
    val postId: Int?,
    @[Json(name = "post_type") PostType]
    val postType: String?,
    @Json(name = "suggested_edit_id")
    val suggestedEditId: Int?,
    @[Json(name = "timeline_type") TimelineType]
    val timelineType: String,
    @Json(name = "title")
    val title: String?,
    @Json(name = "user_id")
    val userId: Int
)
