package me.tylerbwong.stack.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.Single
import me.tylerbwong.stack.data.persistence.entity.QuestionEntity
import me.tylerbwong.stack.data.persistence.entity.UserEntity

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(questions: List<QuestionEntity>)

    @Query("SELECT * FROM question WHERE sortString = :sortString")
    fun get(sortString: String): Single<List<QuestionEntity>>

    @Query("DELETE FROM question WHERE sortString = :sortString")
    fun delete(sortString: String)

    @Transaction
    fun update(questions: List<QuestionEntity>, users: List<UserEntity>, sortString: String, userDao: UserDao) {
        // TODO delete old users
        delete(sortString)
        userDao.insert(users)
        insert(questions)
    }
}
