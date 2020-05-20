package me.tylerbwong.stack.ui.di

import dagger.Component
import io.noties.markwon.Markwon
import me.tylerbwong.stack.data.auth.di.AuthModule
import me.tylerbwong.stack.data.network.di.NetworkModule
import me.tylerbwong.stack.data.persistence.di.PersistenceModule
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.di.StackModule
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.MainViewModelFactory
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
import me.tylerbwong.stack.ui.utils.markdown.Markdown
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AuthModule::class,
        MarkdownModule::class,
        NetworkModule::class,
        PersistenceModule::class,
        StackModule::class,
        UiModule::class
    ]
)
interface UiComponent {
    fun markwon(): Markwon

    fun mainViewModelFactory(): MainViewModelFactory
    fun commentsViewModelFactory(): CommentsViewModelFactory
    fun profileViewModelFactory(): ProfileViewModelFactory
    fun questionsViewModelFactory(): QuestionsViewModelFactory
    fun questionDetailMainViewModelFactory(): QuestionDetailMainViewModelFactory
    fun postAnswerViewModelFactory(): PostAnswerViewModelFactory
    fun searchViewModelFactory(): SearchViewModelFactory
    fun homeViewModelFactory(): HomeViewModelFactory
    fun draftsViewModelFactory(): DraftsViewModelFactory

    fun questionRepository(): QuestionRepository

    fun inject(markdown: Markdown)
    fun inject(activity: BaseActivity)
    fun inject(activity: BaseFragment)

    // TODO Figure out how to remove all of these
    fun inject(activity: MainActivity)
    fun inject(activity: QuestionsActivity)
    fun inject(activity: ProfileActivity)
    fun inject(activity: QuestionDetailActivity)
    fun inject(fragment: QuestionDetailFragment)
    fun inject(fragment: PostAnswerFragment)
    fun inject(fragment: HomeFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: DraftsFragment)
    fun inject(fragment: CommentsBottomSheetDialogFragment)
}
