@file:Suppress("MagicNumber") // For database versions
package me.tylerbwong.stack.data.persistence.di

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StackMigration

@Module
@InstallIn(SingletonComponent::class)
class MigrationModule {

    @[Provides IntoSet StackMigration]
    fun provideAnswerCommentCountColumnMigration(): Migration = object : Migration(8, 9) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE answer ADD COLUMN commentCount INTEGER")
        }
    }
}
