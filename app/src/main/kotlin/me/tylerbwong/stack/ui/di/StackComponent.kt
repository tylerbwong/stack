package me.tylerbwong.stack.ui.di

import androidx.work.WorkManager
import androidx.work.WorkRequest
import coil.ImageLoader
import dagger.Component
import io.noties.markwon.Markwon
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.auth.di.AuthModule
import me.tylerbwong.stack.data.auth.di.SharedPreferencesModule
import me.tylerbwong.stack.data.network.di.NetworkModule
import me.tylerbwong.stack.data.persistence.di.PersistenceModule
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.data.work.WorkModule
import me.tylerbwong.stack.di.DeepLinkModule
import me.tylerbwong.stack.ui.DeepLinkingActivity
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.MainViewModelFactory
import me.tylerbwong.stack.ui.bookmarks.BookmarksFragment
import me.tylerbwong.stack.ui.bookmarks.BookmarksViewModelFactory
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.comments.CommentsViewModelFactory
import me.tylerbwong.stack.ui.drafts.DraftsFragment
import me.tylerbwong.stack.ui.drafts.DraftsViewModelFactory
import me.tylerbwong.stack.ui.home.HomeFragment
import me.tylerbwong.stack.ui.home.HomeViewModelFactory
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.profile.ProfileViewModelFactory
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.questions.QuestionsViewModelFactory
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailFragment
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailMainViewModelFactory
import me.tylerbwong.stack.ui.questions.detail.post.PostAnswerFragment
import me.tylerbwong.stack.ui.questions.detail.post.PostAnswerViewModelFactory
import me.tylerbwong.stack.ui.search.SearchFragment
import me.tylerbwong.stack.ui.search.SearchViewModelFactory
import me.tylerbwong.stack.ui.search.filters.FilterBottomSheetDialogFragment
import me.tylerbwong.stack.ui.search.filters.FilterViewModelFactory
import me.tylerbwong.stack.ui.settings.SettingsFragment
import me.tylerbwong.stack.ui.settings.SettingsViewModelFactory
import me.tylerbwong.stack.ui.settings.sites.SitesFragment
import me.tylerbwong.stack.ui.settings.sites.SitesViewModelFactory
import me.tylerbwong.stack.ui.utils.markdown.Markdown
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AuthModule::class,
        SharedPreferencesModule::class,
        MarkdownModule::class,
        NetworkModule::class,
        PersistenceModule::class,
        DeepLinkModule::class,
        UiModule::class,
        WorkModule::class
    ]
)
interface StackComponent {
    fun authStore(): AuthStore
    fun siteStore(): SiteStore
    fun imageLoader(): ImageLoader
    fun deepLinker(): DeepLinker
    fun markwon(): Markwon
    fun sitesWorkRequest(): WorkRequest
    fun workManager(): WorkManager

    fun mainViewModelFactory(): MainViewModelFactory
    fun commentsViewModelFactory(): CommentsViewModelFactory
    fun profileViewModelFactory(): ProfileViewModelFactory
    fun questionsViewModelFactory(): QuestionsViewModelFactory
    fun questionDetailMainViewModelFactory(): QuestionDetailMainViewModelFactory
    fun postAnswerViewModelFactory(): PostAnswerViewModelFactory
    fun searchViewModelFactory(): SearchViewModelFactory
    fun filterViewModelFactory(): FilterViewModelFactory
    fun homeViewModelFactory(): HomeViewModelFactory
    fun draftsViewModelFactory(): DraftsViewModelFactory
    fun bookmarksViewModelFactory(): BookmarksViewModelFactory
    fun settingsViewModelFactory(): SettingsViewModelFactory
    fun sitesViewModelFactory(): SitesViewModelFactory

    fun questionRepository(): QuestionRepository

    fun inject(markdown: Markdown)

    // TODO Figure out how to remove all of these
    fun inject(activity: MainActivity)
    fun inject(activity: DeepLinkingActivity)
    fun inject(activity: QuestionsActivity)
    fun inject(activity: ProfileActivity)
    fun inject(activity: QuestionDetailActivity)
    fun inject(fragment: QuestionDetailFragment)
    fun inject(fragment: PostAnswerFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: DraftsFragment)
    fun inject(fragment: BookmarksFragment)
    fun inject(fragment: CommentsBottomSheetDialogFragment)
    fun inject(fragment: FilterBottomSheetDialogFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: SitesFragment)
}
