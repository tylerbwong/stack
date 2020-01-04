package me.tylerbwong.stack.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.tylerbwong.stack.data.persistence.entity.AnswerDraftEntity

@Dao
interface AnswerDraftDao {
    @Query("SELECT * FROM answer_drafts ORDER BY updated_date DESC")
    fun getAnswerDrafts(): Flow<List<AnswerDraftEntity>>

    @Query("SELECT * FROM answer_drafts WHERE question_id = :questionId")
    fun getAnswerDraft(questionId: Int): AnswerDraftEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnswerDraft(answerDraftEntity: AnswerDraftEntity)

    @Query("DELETE FROM answer_drafts WHERE question_id = :questionId")
    suspend fun deleteDraftById(questionId: Int)

    @Query("DELETE FROM answer_drafts")
    suspend fun clearDrafts()
}
