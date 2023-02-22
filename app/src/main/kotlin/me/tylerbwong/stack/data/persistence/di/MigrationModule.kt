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

    @[Provides IntoSet StackMigration]
    fun provideSiteRegisteredUserMigration(): Migration = object : Migration(9, 10) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE site ADD COLUMN isUserRegistered INTEGER NOT NULL default 0")
        }
    }

    @[Provides IntoSet StackMigration]
    fun provideAnswerShareLinkMigration(): Migration = object : Migration(10, 11) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE answer ADD COLUMN shareLink TEXT NOT NULL default ''")
        }
    }
}
