package me.tylerbwong.stack.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.tylerbwong.stack.data.persistence.dao.AnswerDao
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDraftDao
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.persistence.dao.SiteDao
import me.tylerbwong.stack.data.persistence.dao.UserDao
import me.tylerbwong.stack.data.persistence.entity.AnswerDraftEntity
import me.tylerbwong.stack.data.persistence.entity.AnswerEntity
import me.tylerbwong.stack.data.persistence.entity.QuestionDraftEntity
import me.tylerbwong.stack.data.persistence.entity.QuestionEntity
import me.tylerbwong.stack.data.persistence.entity.SearchEntity
import me.tylerbwong.stack.data.persistence.entity.SiteEntity
import me.tylerbwong.stack.data.persistence.entity.UserEntity
import me.tylerbwong.stack.data.persistence.typeconverter.ListTypeConverter

@Database(
    entities = [
        QuestionEntity::class,
        AnswerEntity::class,
        UserEntity::class,
        AnswerDraftEntity::class,
        SearchEntity::class,
        SiteEntity::class,
        QuestionDraftEntity::class
    ],
    version = 9
)
@TypeConverters(ListTypeConverter::class)
abstract class StackDatabase : RoomDatabase() {

    abstract fun getQuestionDao(): QuestionDao

    abstract fun getAnswerDao(): AnswerDao

    abstract fun getUserDao(): UserDao

    abstract fun getAnswerDraftDao(): AnswerDraftDao

    abstract fun getSearchDao(): SearchDao

    abstract fun getSiteDao(): SiteDao

    abstract fun getQuestionDraftDao(): QuestionDraftDao
}
