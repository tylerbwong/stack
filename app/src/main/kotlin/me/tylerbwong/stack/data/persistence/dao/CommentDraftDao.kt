package me.tylerbwong.stack.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.tylerbwong.stack.data.persistence.entity.CommentDraftEntity

@Dao
interface CommentDraftDao {

    @Query("SELECT * FROM comment_drafts WHERE post_id = :postId AND site = :site")
    suspend fun getCommentDraft(postId: Int, site: String): CommentDraftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommentDraft(commentDraftEntity: CommentDraftEntity)

    @Query("DELETE FROM comment_drafts WHERE post_id = :postId AND site = :site")
    suspend fun deleteDraftById(postId: Int, site: String)

    @Query("DELETE FROM comment_drafts")
    suspend fun clearDrafts()
}
