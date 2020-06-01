package me.tylerbwong.stack.ui.di

import android.content.Context
import coil.Coil
import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.data.SiteStore
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.SiteService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.network.service.UserService
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.persistence.dao.SiteDao
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.ui.MainViewModelFactory
import me.tylerbwong.stack.ui.bookmarks.BookmarksViewModelFactory
import me.tylerbwong.stack.ui.comments.CommentsViewModelFactory
import me.tylerbwong.stack.ui.drafts.DraftsViewModelFactory
import me.tylerbwong.stack.ui.home.HomeViewModelFactory
import me.tylerbwong.stack.ui.profile.ProfileViewModelFactory
import me.tylerbwong.stack.ui.questions.QuestionsViewModelFactory
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailMainViewModelFactory
import me.tylerbwong.stack.ui.questions.detail.post.PostAnswerViewModelFactory
import me.tylerbwong.stack.ui.search.SearchViewModelFactory
import me.tylerbwong.stack.ui.search.filters.FilterViewModelFactory
import me.tylerbwong.stack.ui.settings.SettingsViewModelFactory
import me.tylerbwong.stack.ui.settings.sites.SitesViewModelFactory

@Module
class UiModule {

    @Provides
    fun provideImageLoader(context: Context) = Coil.imageLoader(context)

    @Provides
    fun provideMainViewModelFactory(
        authRepository: AuthRepository
    ) = MainViewModelFactory(authRepository)

    @Provides
    fun provideCommentsViewModelFactory(
        commentService: CommentService,
        authStore: AuthStore,
        siteStore: SiteStore
    ) = CommentsViewModelFactory(commentService, authStore, siteStore)

    @Provides
    fun provideProfileViewModelFactory(
        userService: UserService
    ) = ProfileViewModelFactory(userService)

    @Provides
    fun provideQuestionsViewModelFactory(
        questionService: QuestionService,
        siteStore: SiteStore
    ) = QuestionsViewModelFactory(questionService, siteStore)

    @Provides
    fun provideQuestionDetailMainViewModelFactory(
        authRepository: AuthRepository,
        siteRepository: SiteRepository,
        questionService: QuestionService
    ) = QuestionDetailMainViewModelFactory(authRepository, siteRepository, questionService)

    @Provides
    fun providePostAnswerViewModelFactory(
        questionService: QuestionService,
        draftDao: AnswerDraftDao,
        siteStore: SiteStore
    ) = PostAnswerViewModelFactory(questionService, draftDao, siteStore)

    @Provides
    fun provideSearchViewModelFactory(
        tagService: TagService,
        searchService: SearchService,
        searchDao: SearchDao,
        siteStore: SiteStore
    ) = SearchViewModelFactory(tagService, searchService, searchDao, siteStore)

    @Provides
    fun provideFilterViewModelFactory() = FilterViewModelFactory()

    @Provides
    fun provideHomeViewModelFactory(
        questionRepository: QuestionRepository,
        siteRepository: SiteRepository
    ) = HomeViewModelFactory(questionRepository, siteRepository)

    @Provides
    fun provideDraftsViewModelFactory(
        draftDao: AnswerDraftDao,
        siteStore: SiteStore
    ) = DraftsViewModelFactory(draftDao, siteStore)

    @Provides
    fun provideBookMarksViewModelFactory(
        authRepository: AuthRepository,
        siteRepository: SiteRepository,
        questionService: QuestionService
    ) = BookmarksViewModelFactory(authRepository, siteRepository, questionService)

    @Provides
    fun provideSettingsViewModelFactory(
        siteRepository: SiteRepository
    ) = SettingsViewModelFactory(siteRepository)

    @Provides
    fun provideSitesViewModelFactor(
        siteRepository: SiteRepository
    ) = SitesViewModelFactory(siteRepository)

    @Provides
    fun provideQuestionRepository(
        questionService: QuestionService
    ) = QuestionRepository(questionService)

    @Provides
    fun provideSiteRepository(
        siteDao: SiteDao,
        siteService: SiteService,
        siteStore: SiteStore
    ) = SiteRepository(siteDao, siteService, siteStore)
}
