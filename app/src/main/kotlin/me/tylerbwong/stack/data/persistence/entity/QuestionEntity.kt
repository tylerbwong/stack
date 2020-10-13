package me.tylerbwong.stack.data.persistence.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "question",
    indices = [Index("owner"), Index("lastEditor")],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["owner"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["lastEditor"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class QuestionEntity(
    val answerCount: Int,
    val body: String?,
    val bodyMarkdown: String?,
    val closedDate: Long?,
    val closedReason: String?,
    val commentCount: Int?,
    val creationDate: Long,
    val downVoteCount: Int,
    val downvoted: Boolean,
    val favoriteCount: Int,
    val favorited: Boolean,
    val isAnswered: Boolean,
    val lastActivityDate: Long?,
    val lastEditDate: Long?,
    val lastEditor: Int?,
    val owner: Int,
    @PrimaryKey
    val questionId: Int,
    val score: Int,
    val shareLink: String,
    val tags: List<String>?,
    val title: String,
    val upVoteCount: Int,
    val upvoted: Boolean,
    val viewCount: Int
)
