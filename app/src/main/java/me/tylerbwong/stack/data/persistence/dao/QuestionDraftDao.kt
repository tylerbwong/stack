package me.tylerbwong.stack.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.tylerbwong.stack.data.persistence.entity.QuestionDraftEntity

@Dao
interface QuestionDraftDao {
    @Query("SELECT * FROM question_drafts WHERE site = :site ORDER BY updated_date DESC")
    fun getQuestionDrafts(site: String): Flow<List<QuestionDraftEntity>>

    @Query("SELECT * FROM question_drafts WHERE id = :id AND site = :site")
    suspend fun getQuestionDraft(id: Int, site: String): QuestionDraftEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestionDraft(questionDraftEntity: QuestionDraftEntity): Long

    @Query("DELETE FROM question_drafts WHERE id = :id AND site = :site")
    suspend fun deleteDraftById(id: Int, site: String)

    @Query("DELETE FROM question_drafts")
    suspend fun clearDrafts()
}
