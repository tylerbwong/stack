package me.tylerbwong.stack.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_drafts")
data class QuestionDraftEntity(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "updated_date")
    val updatedDate: Long,
    @ColumnInfo(name = "body")
    val body: String,
    @ColumnInfo(name = "tags")
    val tags: String,
    @ColumnInfo(name = "site")
    val site: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = -1
}
