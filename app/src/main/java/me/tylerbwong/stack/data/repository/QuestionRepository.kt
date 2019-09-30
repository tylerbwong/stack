package me.tylerbwong.stack.data.repository

import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.network.ServiceProvider

class QuestionRepository {

    suspend fun getQuestions(@Sort sort: String): List<Question> {
        return getQuestionsFromNetwork(sort)
    }

    private suspend fun getQuestionsFromNetwork(@Sort sort: String): List<Question> {
        return ServiceProvider.questionService.getQuestions(sort = sort).items
    }
}
