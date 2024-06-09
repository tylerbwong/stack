package me.tylerbwong.stack.api.model

import androidx.annotation.StringDef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable
data class TimelineEvent(
    @SerialName("badge_id")
    val badgeId: Int?,
    @SerialName("comment_id")
    val commentId: Int?,
    @SerialName("creation_date")
    val creationDate: Long,
    @SerialName("detail")
    val detail: String?,
    @SerialName("link")
    val link: String,
    @SerialName("post_id")
    val postId: Int?,
    @[SerialName("post_type") PostType]
    val postType: String?,
    @SerialName("suggested_edit_id")
    val suggestedEditId: Int?,
    @[SerialName("timeline_type") TimelineType]
    val timelineType: String,
    @SerialName("title")
    val title: String?,
    @SerialName("user_id")
    val userId: Int
)
