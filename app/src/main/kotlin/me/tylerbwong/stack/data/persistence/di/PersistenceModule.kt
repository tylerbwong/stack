package me.tylerbwong.stack.data.persistence.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.persistence.typeconverter.ListTypeConverter
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RoomTypeConverter

@Module
@InstallIn(SingletonComponent::class)
class PersistenceModule {

    @[Provides Singleton]
    fun provideStackDatabase(
        @ApplicationContext context: Context,
        @StackMigration migrations: Set<@JvmSuppressWildcards Migration>,
        @RoomTypeConverter typeConverters: Set<@JvmSuppressWildcards Any>,
    ): StackDatabase {
        val builder = Room.databaseBuilder(context, StackDatabase::class.java, STACK_DATABASE_NAME)
        migrations.forEach { builder.addMigrations((it)) }
        typeConverters.forEach { builder.addTypeConverter(it) }
        return builder.build()
    }

    @[Provides IntoSet RoomTypeConverter]
    fun provideListTypeConverter(moshi: Moshi): Any = ListTypeConverter(moshi)

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
