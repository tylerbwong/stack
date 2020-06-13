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
import me.tylerbwong.stack.ui.DeepLinkingActivity
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.bookmarks.BookmarksFragment
import me.tylerbwong.stack.ui.comments.CommentsBottomSheetDialogFragment
import me.tylerbwong.stack.ui.drafts.DraftsFragment
import me.tylerbwong.stack.ui.home.HomeFragment
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.questions.QuestionsActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailFragment
import me.tylerbwong.stack.ui.questions.detail.post.PostAnswerFragment
import me.tylerbwong.stack.ui.search.SearchFragment
import me.tylerbwong.stack.ui.search.filters.FilterBottomSheetDialogFragment
import me.tylerbwong.stack.ui.settings.SettingsFragment
import me.tylerbwong.stack.ui.settings.sites.SitesFragment
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
