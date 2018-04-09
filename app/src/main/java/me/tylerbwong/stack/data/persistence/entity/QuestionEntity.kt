package me.tylerbwong.stack.data.persistence.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "question", foreignKeys = [
ForeignKey(entity = UserEntity::class, parentColumns = ["userId"], childColumns = ["owner"], onDelete = ForeignKey.CASCADE),
ForeignKey(entity = UserEntity::class, parentColumns = ["userId"], childColumns = ["lastEditor"], onDelete = ForeignKey.CASCADE)])
data class QuestionEntity(
        val answerCount: Int,
        val body: String?,
        val bodyMarkdown: String?,
        val closedDate: Long?,
        val closedReason: String?,
        val commentCount: Int?,
        val creationDate: Long,
        val downVoteCount: Int,
        val favoriteCount: Int,
        val isAnswered: Boolean,
        val lastActivityDate: Long?,
        val lastEditDate: Long?,
        val lastEditor: Int?,
        val owner: Int,
        val questionId: Int,
        val score: Int,
        val shareLink: String,
        val tags: List<String>?,
        val title: String,
        val upVoteCount: Int,
        val viewCount: Int,
        val sortString: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}