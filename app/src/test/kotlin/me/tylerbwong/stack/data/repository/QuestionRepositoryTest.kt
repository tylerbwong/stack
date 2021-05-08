package me.tylerbwong.stack.data.repository

import kotlinx.coroutines.runBlocking
import me.tylerbwong.stack.BaseTest
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.persistence.dao.AnswerDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDao
import me.tylerbwong.stack.data.persistence.dao.UserDao
import me.tylerbwong.stack.data.persistence.entity.QuestionEntity
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@Ignore("Re-enable once offline is enabled")
class QuestionRepositoryTest : BaseTest() {

    @Mock
    private lateinit var questionService: QuestionService

    @Mock
    private lateinit var questionDao: QuestionDao

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var answerDao: AnswerDao

    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var repository: QuestionRepository

    @Before
    fun setUp() {
        repository = QuestionRepository(
            questionService,
            questionDao,
            userDao,
            answerDao,
            authRepository
        )
    }

    @Test
    fun `syncBookmarks returns empty list if not authenticated`() {
        runBlocking {
            whenever(authRepository.isAuthenticated).thenReturn(false)
            val result = repository.syncBookmarks()
            assertTrue(result.isEmpty())
        }
    }

    @Test
    fun `syncBookmarks clears db if network response is empty`() {
        runBlocking {
            whenever(authRepository.isAuthenticated).thenReturn(true)
            whenever(questionService.getBookmarks()).thenReturn(Response(emptyList()))
            repository.syncBookmarks()
            verify(questionDao, times(1)).clearQuestions()
            verify(userDao, times(1)).clearUsers()
            verify(answerDao, times(1)).clearAnswers()
        }
    }

    @Test
    fun `syncBookmarks inserts questions, users, and answers on successful response`() {
        runBlocking {
            val response = Response(
                items = listOf(
                    Question(
                        body = "",
                        bodyMarkdown = "",
                        closedDate = 0L,
                        closedReason = "",
                        commentCount = 0,
                        creationDate = 0L,
                        downVoteCount = 0,
                        isDownVoted = false,
                        isBookmarked = true,
                        bookmarkCount = 0,
                        isAnswered = false,
                        lastActivityDate = 0L,
                        lastEditDate = 0L,
                        lastEditor = null,
                        owner = User(
                            aboutMe = "",
                            acceptRate = null,
                            accountId = 0,
                            displayName = "Tyler Wong",
                            link = null,
                            location = "San Francisco, CA",
                            profileImage = null,
                            reputation = 12,
                            userId = 0,
                            userType = "",
                            badgeCounts = null,
                        ),
                        questionId = 0,
                        score = 0,
                        shareLink = "",
                        tags = null,
                        title = "",
                        upVoteCount = 0,
                        isUpVoted = false,
                        viewCount = 0,
                    )
                )
            )
            whenever(authRepository.isAuthenticated).thenReturn(true)
            whenever(questionService.getBookmarks()).thenReturn(response)
            repository.syncBookmarks()
            verify(questionDao, times(1)).insert(any<QuestionEntity>())
            verify(userDao, times(1)).insert(any())
            verify(answerDao, times(1)).insert(any())
        }
    }
}
