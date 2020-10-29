package me.tylerbwong.stack.data.persistence.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.tylerbwong.stack.data.persistence.StackDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

    @[Provides Singleton Suppress("SpreadOperator")]
    fun provideStackDatabase(
        @ApplicationContext context: Context,
        @StackMigration migrations: Set<@JvmSuppressWildcards Migration>
    ): StackDatabase {
        return Room.databaseBuilder(context, StackDatabase::class.java, STACK_DATABASE_NAME)
            .addMigrations(*migrations.toTypedArray())
            .build()
    }

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
