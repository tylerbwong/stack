package me.tylerbwong.stack.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.tylerbwong.stack.data.persistence.entity.AnswerEntity

@Dao
interface AnswerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(answers: List<AnswerEntity>)

    @Query("SELECT * FROM answer WHERE answerId = :answerId")
    suspend fun get(answerId: Int): AnswerEntity

    @Query("DELETE FROM answer WHERE answerId = :answerId")
    suspend fun delete(answerId: Int)
}
