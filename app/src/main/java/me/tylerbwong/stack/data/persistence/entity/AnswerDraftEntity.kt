package me.tylerbwong.stack.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "answer_drafts")
data class AnswerDraftEntity(
    @ColumnInfo(name = "question_id")
    val questionId: Int,
    @ColumnInfo(name = "updated_date")
    val updatedDate: Long,
    @ColumnInfo(name = "body_markdown")
    val bodyMarkdown: String
) {
    @ColumnInfo(name = "draft_id")
    @PrimaryKey(autoGenerate = true)
    var draftId: Long? = null
}