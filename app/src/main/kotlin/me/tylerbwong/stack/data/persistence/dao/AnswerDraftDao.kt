package me.tylerbwong.stack.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.tylerbwong.stack.data.persistence.entity.AnswerDraftEntity

@Dao
interface AnswerDraftDao {
    @Query("SELECT * FROM answer_drafts WHERE site = :site ORDER BY updated_date DESC")
    fun getAnswerDrafts(site: String): Flow<List<AnswerDraftEntity>>

    @Query("SELECT * FROM answer_drafts WHERE question_id = :questionId AND site = :site")
    suspend fun getAnswerDraft(questionId: Int, site: String): AnswerDraftEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswerDraft(answerDraftEntity: AnswerDraftEntity)

    @Query("DELETE FROM answer_drafts WHERE question_id = :questionId AND site = :site")
    suspend fun deleteDraftById(questionId: Int, site: String)

    @Query("DELETE FROM answer_drafts")
    suspend fun clearDrafts()
}
