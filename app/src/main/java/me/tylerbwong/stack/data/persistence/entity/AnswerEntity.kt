package me.tylerbwong.stack.data.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer")
data class AnswerEntity(
    @PrimaryKey val answerId: Int,
    val isAccepted: Boolean,
    val downVoteCount: Int,
    val upVoteCount: Int,
    val score: Int,
    val creationDate: Long,
    val bodyMarkdown: String
)
