package me.tylerbwong.stack.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.api.model.Answer
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Response
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

    suspend fun getBookmarks(): List<Question> {
        return withContext(Dispatchers.IO) {
            syncBookmarks()
            questionDao.getBookmarks()
                .map { entity ->
                    entity.toQuestion(
                        owner = userDao.get(entity.owner),
                        lastEditor = entity.lastEditor?.let { userDao.get(it) }
                    )
                }
        }
    }

    suspend fun syncBookmarks(): List<Question> {
        return if (authRepository.isAuthenticated) {
            try {
                val bookmarks = questionService.getBookmarks().items
                if (bookmarks.isNotEmpty()) {
                    bookmarks.forEach { question -> saveQuestion(question) }
                } else {
                    clearAll()
                }
                bookmarks
            } catch (ex: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    suspend fun saveQuestion(question: Question): Boolean {
        try {
            val questionUsers = listOfNotNull(question.owner, question.lastEditor)
                .map { user -> user.toUserEntity() }
            userDao.insert(questionUsers)
            val answers = safeCall {
                questionService.getQuestionAnswers(question.questionId)
            }
            answers.forEach { answer ->
                val answerUsers = listOfNotNull(answer.owner, answer.lastEditor)
                    .map { user -> user.toUserEntity() }
                userDao.insert(answerUsers)
            }
            answerDao.insert(answers.map { answer -> answer.toAnswerEntity() })
            questionDao.insert(question.toQuestionEntity())
        } catch (ex: Exception) {
            clearAll()
            return false
        }
        return true
    }

    suspend fun removeQuestion(question: Question): Boolean {
        try {
            listOfNotNull(question.owner, question.lastEditor)
                .forEach { userDao.delete(it.userId) }
            answerDao.deleteByQuestionId(question.questionId)
            questionDao.delete(question.questionId)
        } catch (ex: Exception) {
            clearAll()
            return false
        }
        return true
    }

    private suspend fun clearAll() {
        questionDao.clearQuestions()
        userDao.clearUsers()
        answerDao.clearAnswers()
    }

    private suspend fun getQuestionsFromNetwork(@Sort sort: String): List<Question> {
        return questionService.getQuestions(sort = sort).items
    }

    private suspend fun <T> safeCall(block: suspend () -> Response<T>): List<T> {
        return try {
            block().items
        } catch (ex: Exception) {
            emptyList()
        }
    }
}
