package me.tylerbwong.stack.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.tylerbwong.stack.data.persistence.dao.QuestionDao
import me.tylerbwong.stack.data.persistence.dao.UserDao
import me.tylerbwong.stack.data.persistence.entity.QuestionEntity
import me.tylerbwong.stack.data.persistence.entity.UserEntity
import me.tylerbwong.stack.data.persistence.typeconverter.ListTypeConverter

@Database(entities = [QuestionEntity::class, UserEntity::class], version = 1)
@TypeConverters(ListTypeConverter::class)
abstract class StackDatabase : RoomDatabase() {

    companion object {
        private const val stackDatabaseName = "stack-database"

        private lateinit var stackDatabaseInstance: StackDatabase

        fun init(context: Context) {
            stackDatabaseInstance = Room.databaseBuilder(
                    context,
                    StackDatabase::class.java,
                    stackDatabaseName
            ).build()
        }

        @Synchronized
        fun getInstance(): StackDatabase = stackDatabaseInstance
    }

    abstract fun getQuestionDao(): QuestionDao

    abstract fun getUserDao(): UserDao
}
