package me.tylerbwong.stack.ui

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.Lazy
import dagger.hilt.android.HiltAndroidApp
import me.tylerbwong.stack.ui.theme.ThemeManager
import okhttp3.OkHttpClient
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class StackApplication : Application(), Configuration.Provider, ImageLoaderFactory {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var tree: Timber.Tree

    @Inject
    lateinit var okHttpClient: Lazy<OkHttpClient>

    override fun onCreate() {

        if (ProcessPhoenix.isPhoenixProcess(this)) {
            return
        }

        super.onCreate()

        ApplicationWrapper.init(this)

        ThemeManager.init(this)

        Timber.plant(tree)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .okHttpClient {
                okHttpClient.get().newBuilder()
                    .cache(CoilUtils.createDefaultCache(this))
                    .build()
            }
            .build()
    }
}
