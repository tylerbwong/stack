package me.tylerbwong.stack.data.repository

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.withContext
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider
import me.tylerbwong.stack.data.persistence.StackDatabase
import me.tylerbwong.stack.data.persistence.entity.QuestionEntity
import me.tylerbwong.stack.data.persistence.entity.UserEntity
import me.tylerbwong.stack.data.toQuestion
import me.tylerbwong.stack.data.toQuestionEntity
import me.tylerbwong.stack.data.toUserEntity
import me.tylerbwong.stack.data.utils.concat

class QuestionRepository(private val stackDatabase: StackDatabase = StackDatabase.getInstance()) {

    private val questionDao by lazy { stackDatabase.getQuestionDao() }
    private val userDao by lazy { stackDatabase.getUserDao() }

    suspend fun getQuestions(sort: String): ReceiveChannel<List<Question>> {
        return concat(
                getQuestionsFromDb(sort),
                getQuestionsFromNetwork(sort)
        )
    }

    private suspend fun getQuestionsFromDb(@Sort sort: String): Deferred<List<Question>> {
        return withContext(Dispatchers.IO) {
            async {
                questionDao.get(sort)
                        .map { questionEntity ->
                            questionEntity.toQuestion(
                                    userDao.get(questionEntity.owner),
                                    questionEntity.lastEditor?.let { userDao.get(it) }
                            )
                        }
            }
        }
    }

    private suspend fun getQuestionsFromNetwork(@Sort sort: String): Deferred<List<Question>> {
        return withContext(Dispatchers.IO) {
            async {
                ServiceProvider.questionService.getQuestions(sort = sort)
                        .await()
                        .items
                        .also { saveQuestions(it, sort) }
            }
        }
    }

    private suspend fun saveQuestions(questions: List<Question>, @Sort sortString: String) {
        val userEntities = mutableListOf<UserEntity>()
        val questionEntities = mutableListOf<QuestionEntity>()
        questions.forEach { question ->
            userEntities.add(question.owner.toUserEntity())
            question.lastEditor?.let { userEntities.add(it.toUserEntity()) }
            questionEntities.add(question.toQuestionEntity(sortString))
        }
        updateQuestionsAndUsers(questionEntities, userEntities, sortString)
    }

    private suspend fun updateQuestionsAndUsers(
            questions: List<QuestionEntity>,
            users: List<UserEntity>,
            sortString: String
    ) {
        // TODO delete old users
        questionDao.delete(sortString)
        userDao.insert(users)
        questionDao.insert(questions)
    }
}
