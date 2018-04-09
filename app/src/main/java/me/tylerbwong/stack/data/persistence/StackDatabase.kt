package me.tylerbwong.stack.data.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
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

        private var stackDatabaseInstance: StackDatabase? = null

        @Synchronized
        fun getInstance(context: Context): StackDatabase = stackDatabaseInstance ?: Room.databaseBuilder(context.applicationContext, StackDatabase::class.java, stackDatabaseName).build()
    }

    abstract fun getQuestionDao(): QuestionDao

    abstract fun getUserDao(): UserDao
}