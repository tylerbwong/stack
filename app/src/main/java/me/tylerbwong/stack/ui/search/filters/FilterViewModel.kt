package me.tylerbwong.stack.ui.search.filters

import androidx.lifecycle.ViewModel
import me.tylerbwong.stack.data.model.SearchPayload

class FilterViewModel : ViewModel() {

    internal var currentPayload = SearchPayload.Standard("")
    internal var hasAcceptedAnswer = false
        set(value) {
            field = value
            currentPayload = currentPayload?.copy(isAccepted = field)
        }
    internal var isClosed = false
        set(value) {
            field = value
            currentPayload = currentPayload?.copy(isClosed = field)
        }
    internal var minimumAnswers = 0
        set(value) {
            field = value
            currentPayload = currentPayload?.copy(minNumAnswers = field)
        }
    internal var titleContains = ""
        set(value) {
            field = value
            currentPayload = currentPayload?.copy(titleContains = field)
        }
    internal var bodyContains = ""
        set(value) {
            field = value
            currentPayload = currentPayload?.copy(bodyContains = field)
        }
    internal var tags = ""
        set(value) {
            field = value
            currentPayload = currentPayload?.copy(tags = field.split(","))
        }
}
