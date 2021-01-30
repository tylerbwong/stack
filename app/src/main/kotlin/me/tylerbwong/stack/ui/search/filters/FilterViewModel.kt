package me.tylerbwong.stack.ui.search.filters

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import me.tylerbwong.stack.data.model.SearchPayload
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {

    internal var updatePayloadListener: UpdatePayloadListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }
    internal var currentPayload: SearchPayload? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }
    internal var hasAcceptedAnswer: Boolean? = null
        set(value) {
            field = value
            currentPayload = currentPayload?.copy(isAccepted = field)
        }
    internal var isClosed: Boolean? = null
        set(value) {
            field = value
            currentPayload = currentPayload?.copy(isClosed = field)
        }
    internal var minimumAnswers: Int? = null
        set(value) {
            field = value
            currentPayload = currentPayload?.copy(minNumAnswers = field)
        }
    internal var titleContains: String? = null
        set(value) {
            field = if (value.isNullOrEmpty()) null else value
            currentPayload = currentPayload?.copy(titleContains = field)
        }
    internal var bodyContains: String? = null
        set(value) {
            field = if (value.isNullOrEmpty()) null else value
            currentPayload = currentPayload?.copy(bodyContains = field)
        }
    internal var tags: String? = null
        set(value) {
            field = if (value.isNullOrEmpty()) null else value
            currentPayload = currentPayload?.copy(tags = field?.split(","))
        }

    fun applyFilters() {
        currentPayload?.let {
            updatePayloadListener?.invoke(it)
        }
    }

    fun clearFilters() {
        hasAcceptedAnswer = null
        isClosed = null
        minimumAnswers = null
        titleContains = null
        bodyContains = null
        tags = null
        applyFilters()
    }
}
