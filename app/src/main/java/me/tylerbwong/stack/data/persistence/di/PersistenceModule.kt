package me.tylerbwong.stack.data.persistence.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.persistence.StackDatabase
import javax.inject.Named
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Singleton
    @Provides
    fun provideSiteStore(
        @Named("siteSharedPreferences")
        sharedPreferences: SharedPreferences
    ) = SiteStore(sharedPreferences)

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

    companion object {
        private const val STACK_DATABASE_NAME = "stack-database"
    }
}
