package me.tylerbwong.stack.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.api.model.Answer
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Sort
import me.tylerbwong.stack.api.service.QuestionService
import me.tylerbwong.stack.data.auth.AuthRepository
import me.tylerbwong.stack.data.persistence.dao.AnswerDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDao
import me.tylerbwong.stack.data.persistence.dao.UserDao
import me.tylerbwong.stack.data.toAnswer
import me.tylerbwong.stack.data.toAnswerEntity
import me.tylerbwong.stack.data.toQuestion
import me.tylerbwong.stack.data.toQuestionEntity
import me.tylerbwong.stack.data.toUserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(
    private val questionService: QuestionService,
    private val questionDao: QuestionDao,
    private val userDao: UserDao,
    private val answerDao: AnswerDao,
    private val authRepository: AuthRepository
) {
    suspend fun getQuestions(@Sort sort: String): List<Question> {
        return getQuestionsFromNetwork(sort)
    }

    suspend fun getQuestion(questionId: Int): Question {
        return if (authRepository.isAuthenticated) {
            withContext(Dispatchers.IO) {
                questionDao.get(questionId)?.let { entity ->
                    entity.toQuestion(
                        owner = userDao.get(entity.owner),
                        lastEditor = entity.lastEditor?.let { userDao.get(it) }
                    )
                }
            } ?: questionService.getQuestionDetailsAuth(questionId).items.first()
        } else {
            questionService.getQuestionDetails(questionId).items.first()
        }
    }

    suspend fun getQuestionAnswers(questionId: Int): List<Answer> {
        val answerEntities = withContext(Dispatchers.IO) {
            answerDao.getAnswersByQuestionId(questionId)
        }
        return if (answerEntities.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                answerEntities.map { entity ->
                    entity.toAnswer(
                        owner = userDao.get(entity.owner),
                        lastEditor = entity.lastEditor?.let { userDao.get(it) }
                    )
                }
            }
        } else {
            questionService.getQuestionAnswers(questionId).items
        }.sortedBy { !it.isAccepted }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getBookmarks(): Flow<List<Question>> {
        return questionDao.getBookmarks()
            .map { questionEntities ->
                questionEntities.map { entity ->
                    entity.toQuestion(
                        owner = userDao.get(entity.owner),
                        lastEditor = entity.lastEditor?.let { userDao.get(it) }
                    )
                }
            }
            .onStart { emit(syncBookmarks()) }
            .onEmpty { emit(syncBookmarks()) }
            .flowOn(Dispatchers.IO)
    }

    suspend fun syncBookmarks(): List<Question> {
        return if (authRepository.isAuthenticated) {
            val bookmarks = try {
                questionService.getBookmarks().items
            } catch (ex: Exception) {
                emptyList()
            }
            bookmarks.forEach { question ->
                val questionUsers = listOfNotNull(question.owner, question.lastEditor)
                    .map { user -> user.toUserEntity() }
                userDao.insert(questionUsers)
                val answers = questionService.getQuestionAnswers(question.questionId).items
                answers.forEach { answer ->
                    val answerUsers = listOfNotNull(answer.owner, answer.lastEditor)
                        .map { user -> user.toUserEntity() }
                    userDao.insert(answerUsers)
                }
                answerDao.insert(answers.map { answer -> answer.toAnswerEntity() })
            }
            val entities = bookmarks.map { it.toQuestionEntity() }
            if (entities.isNotEmpty()) {
                questionDao.insert(entities)
            } else {
                questionDao.clearQuestions()
            }
            bookmarks
        } else {
            emptyList()
        }
    }

    private suspend fun getQuestionsFromNetwork(@Sort sort: String): List<Question> {
        return questionService.getQuestions(sort = sort).items
    }
}
