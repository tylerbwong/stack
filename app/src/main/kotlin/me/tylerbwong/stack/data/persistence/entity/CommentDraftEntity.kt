package me.tylerbwong.stack.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comment_drafts")
data class CommentDraftEntity(
    @ColumnInfo(name = "post_id")
    @PrimaryKey
    val postId: Int,
    @ColumnInfo(name = "body_markdown")
    val bodyMarkdown: String,
    @ColumnInfo(name = "site")
    val site: String
)
