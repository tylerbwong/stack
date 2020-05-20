package me.tylerbwong.stack.ui.di

import dagger.Module
import dagger.Provides
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.network.service.CommentService
import me.tylerbwong.stack.data.network.service.QuestionService
import me.tylerbwong.stack.data.network.service.SearchService
import me.tylerbwong.stack.data.network.service.TagService
import me.tylerbwong.stack.data.network.service.UserService
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.ui.MainViewModelFactory
import me.tylerbwong.stack.ui.comments.CommentsViewModelFactory
import me.tylerbwong.stack.ui.drafts.DraftsViewModelFactory
import me.tylerbwong.stack.ui.home.HomeViewModelFactory
import me.tylerbwong.stack.ui.profile.ProfileViewModelFactory
import me.tylerbwong.stack.ui.questions.QuestionsViewModelFactory
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailMainViewModelFactory
import me.tylerbwong.stack.ui.questions.detail.post.PostAnswerViewModelFactory
import me.tylerbwong.stack.ui.search.SearchViewModelFactory
import me.tylerbwong.stack.ui.search.filters.FilterViewModelFactory

@Module
class UiModule {

    @Provides
    fun provideMainViewModelFactory(
        authRepository: AuthRepository
    ) = MainViewModelFactory(authRepository)

    @Provides
    fun provideCommentsViewModelFactory(
        commentService: CommentService,
        authStore: AuthStore
    ) = CommentsViewModelFactory(commentService, authStore)

    @Provides
    fun provideProfileViewModelFactory(
        userService: UserService
    ) = ProfileViewModelFactory(userService)

    @Provides
    fun provideQuestionsViewModelFactory(
        questionService: QuestionService
    ) = QuestionsViewModelFactory(questionService)

    @Provides
    fun provideQuestionDetailMainViewModelFactory(
        authStore: AuthStore,
        questionService: QuestionService
    ) = QuestionDetailMainViewModelFactory(authStore, questionService)

    @Provides
    fun providePostAnswerViewModelFactory(
        questionService: QuestionService,
        draftDao: AnswerDraftDao
    ) = PostAnswerViewModelFactory(questionService, draftDao)

    @Provides
    fun provideSearchViewModelFactory(
        tagService: TagService,
        searchService: SearchService,
        searchDao: SearchDao
    ) = SearchViewModelFactory(tagService, searchService, searchDao)

    @Provides
    fun provideFilterViewModelFactory() = FilterViewModelFactory()

    @Provides
    fun provideHomeViewModelFactory(
        questionRepository: QuestionRepository
    ) = HomeViewModelFactory(questionRepository)

    @Provides
    fun provideDraftsViewModelFactory(
        draftDao: AnswerDraftDao
    ) = DraftsViewModelFactory(draftDao)

    @Provides
    fun provideQuestionRepository(
        questionService: QuestionService
    ) = QuestionRepository(questionService)
}
