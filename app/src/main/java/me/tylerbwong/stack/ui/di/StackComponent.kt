package me.tylerbwong.stack.ui.di

import androidx.work.WorkManager
import androidx.work.WorkRequest
import dagger.Component
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.auth.di.SharedPreferencesModule
import me.tylerbwong.stack.data.network.di.NetworkModule
import me.tylerbwong.stack.data.persistence.di.PersistenceModule
import me.tylerbwong.stack.data.work.WorkModule
import me.tylerbwong.stack.ui.utils.markdown.Markdown
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        SharedPreferencesModule::class,
        MarkdownModule::class,
        NetworkModule::class,
        PersistenceModule::class,
        UiModule::class,
        WorkModule::class
    ]
)
interface StackComponent {
    fun authStore(): AuthStore
    fun siteStore(): SiteStore
    fun deepLinker(): DeepLinker
    fun sitesWorkRequest(): WorkRequest
    fun workManager(): WorkManager

    fun inject(markdown: Markdown)
}
