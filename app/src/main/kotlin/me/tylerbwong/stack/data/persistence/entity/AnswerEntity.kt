package me.tylerbwong.stack.data.persistence.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "answer",
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
data class AnswerEntity(
    @PrimaryKey val answerId: Int,
    val isAccepted: Boolean,
    val downVoteCount: Int,
    val upVoteCount: Int,
    val score: Int,
    val creationDate: Long,
    val bodyMarkdown: String,
    val questionId: Int,
    val owner: Int,
    val lastEditDate: Long?,
    val lastEditor: Int?,
    val commentCount: Int?
)
