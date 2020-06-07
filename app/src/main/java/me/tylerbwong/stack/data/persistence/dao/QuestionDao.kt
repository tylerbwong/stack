package me.tylerbwong.stack.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.tylerbwong.stack.data.persistence.entity.QuestionEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(questions: List<QuestionEntity>)

    @Query("SELECT * FROM question WHERE sortString = :sortString")
    suspend fun get(sortString: String): List<QuestionEntity>

    @Query("SELECT * FROM question WHERE favorited = 1")
    fun getBookmarks(): Flow<List<QuestionEntity>>

    @Query("DELETE FROM question WHERE sortString = :sortString")
    suspend fun delete(sortString: String)
}
