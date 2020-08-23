package me.tylerbwong.stack.data.persistence.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.data.persistence.StackDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

    @Singleton
    @Provides
    fun provideStackDatabase(context: Context) = Room.databaseBuilder(
        context,
        StackDatabase::class.java,
        STACK_DATABASE_NAME
    ).build()

    @Provides
    fun provideQuestionDao(stackDatabase: StackDatabase) = stackDatabase.getQuestionDao()

    @Provides
    fun provideAnswerDao(stackDatabase: StackDatabase) = stackDatabase.getAnswerDao()

    @Provides
    fun provideUserDao(stackDatabase: StackDatabase) = stackDatabase.getUserDao()

    @Provides
    fun provideAnswerDraftDao(stackDatabase: StackDatabase) = stackDatabase.getAnswerDraftDao()

    @Provides
    fun provideSearchDao(stackDatabase: StackDatabase) = stackDatabase.getSearchDao()

    @Provides
    fun provideSiteDao(stackDatabase: StackDatabase) = stackDatabase.getSiteDao()

    @Provides
    fun provideQuestionDraftDao(stackDatabase: StackDatabase) = stackDatabase.getQuestionDraftDao()

    companion object {
        private const val STACK_DATABASE_NAME = "stack-database"
    }
}
