package me.tylerbwong.stack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.api.model.HOT
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.Sort
import me.tylerbwong.stack.data.content.ContentFilter
import me.tylerbwong.stack.data.repository.QuestionRepository
import me.tylerbwong.stack.data.repository.SiteRepository
import me.tylerbwong.stack.ui.BaseViewModel
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val repository: QuestionRepository,
    private val siteRepository: SiteRepository,
    private val contentFilter: ContentFilter,
) : BaseViewModel() {

    internal val questions: LiveData<List<Question>>
        get() = _questions
    private val _questions = MutableLiveData<List<Question>>()

    internal val siteLiveData: LiveData<String>
        get() = siteRepository.siteLiveData

    internal val contentFilterUpdated: LiveData<ContentFilter.ContentFilterData>
        get() = contentFilter.contentFilteredUpdated

    @Sort
    internal var currentSort: String = HOT

    internal fun fetchQuestions(@Sort sort: String = currentSort) {
        currentSort = sort
        launchRequest {
            _questions.value = repository.getQuestions(sort)
        }
    }
}
