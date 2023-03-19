package me.tylerbwong.stack.data.content

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import me.tylerbwong.stack.api.model.Answer
import me.tylerbwong.stack.api.model.Comment
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.data.di.StackSharedPreferences
import me.tylerbwong.stack.data.site.SiteStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Temporary solution. Storing ids as "{id},{site}".
 */
@Singleton
class ContentFilter @Inject constructor(
    private val siteStore: SiteStore,
    @StackSharedPreferences private val preferences: SharedPreferences,
) {
    val contentFilteredUpdated: LiveData<Unit>
        get() = _contentFilterUpdated
    private val _contentFilterUpdated = MutableLiveData<Unit>()

    val filteredQuestionIds: Set<Int>
        get() = getFilteredContent(QUESTION_ID_CONTENT_FILTER)

    val filteredAnswerIds: Set<Int>
        get() = getFilteredContent(ANSWER_ID_CONTENT_FILTER)

    val filteredCommentIds: Set<Int>
        get() = getFilteredContent(COMMENT_ID_CONTENT_FILTER)

    val filteredUserIds: Set<Int>
        get() = getFilteredContent(USER_ID_CONTENT_FILTER)

    fun addFilteredQuestionId(questionId: Int) {
        addFilteredContentId(questionId, QUESTION_ID_CONTENT_FILTER) { filteredQuestionIds }
    }

    fun addFilteredAnswerId(answerId: Int) {
        addFilteredContentId(answerId, ANSWER_ID_CONTENT_FILTER) { filteredAnswerIds }
    }

    fun addFilteredCommentId(commentId: Int) {
        addFilteredContentId(commentId, COMMENT_ID_CONTENT_FILTER) { filteredCommentIds }
    }

    fun addFilteredUserId(userId: Int) {
        addFilteredContentId(userId, USER_ID_CONTENT_FILTER) { filteredUserIds }
    }

    fun clearFilteredQuestionIds() {
        clearFilteredContent(QUESTION_ID_CONTENT_FILTER)
    }

    fun clearFilteredAnswerIds() {
        clearFilteredContent(ANSWER_ID_CONTENT_FILTER)
    }

    fun clearFilteredCommentIds() {
        clearFilteredContent(COMMENT_ID_CONTENT_FILTER)
    }

    fun clearFilteredUserIds() {
        clearFilteredContent(USER_ID_CONTENT_FILTER)
    }

    fun List<Question>.applyQuestionFilter(): List<Question> {
        val questionIds = filteredQuestionIds
        val userIds = filteredUserIds
        return this
//            .filter { it.questionId !in questionIds }
//            .filter { it.owner.userId !in userIds }
    }

    fun List<Answer>.applyAnswerFilter(): List<Answer> {
        val answerIds = filteredAnswerIds
        val userIds = filteredUserIds
        return this
//            .filter { it.answerId !in answerIds }
//            .filter { it.owner.userId !in userIds }
    }

    fun List<Comment>.applyCommentFilter(): List<Comment> {
        val commentIds = filteredCommentIds
        val userIds = filteredUserIds
        return this
//            .filter { it.commentId !in commentIds }
//            .filter { it.owner.userId !in userIds }
    }

    private fun getFilteredContent(key: String): Set<Int> {
        return preferences.getStringSet(key, emptySet())
            ?.mapNotNull {
                val (id, site) = it.split(",")
                if (siteStore.site == site) {
                    id.toInt()
                } else {
                    null
                }
            }
            ?.toSet() ?: emptySet()
    }

    private fun addFilteredContentId(id: Int, key: String, initialContent: () -> Set<Int>) {
        if (id != -1) {
            val currentIds = initialContent()
            preferences.edit()
                .putStringSet(key, (currentIds + id).map { "${it},${siteStore.site}" }.toSet())
                .apply()
            _contentFilterUpdated.value = Unit
        }
    }

    private fun clearFilteredContent(key: String) {
        preferences.edit()
            .putStringSet(key, emptySet())
            .apply()
        _contentFilterUpdated.value = Unit
    }

    companion object {
        private const val QUESTION_ID_CONTENT_FILTER = "content_filter_question_id"
        private const val ANSWER_ID_CONTENT_FILTER = "content_filter_answer_id"
        private const val COMMENT_ID_CONTENT_FILTER = "content_filter_comment_id"
        private const val USER_ID_CONTENT_FILTER = "content_filter_user_id"
    }
}
