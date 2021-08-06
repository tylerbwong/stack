package me.tylerbwong.stack.play.logging.di

import android.content.Context
import com.google.firebase.FirebaseApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import me.tylerbwong.stack.data.di.Initializer

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @[Provides Initializer IntoSet]
    fun provideFirebaseInitializer(@ApplicationContext context: Context): () -> Unit = {
        FirebaseApp.initializeApp(context)
    }
}
