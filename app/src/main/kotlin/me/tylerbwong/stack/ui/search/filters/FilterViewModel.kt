package me.tylerbwong.stack.ui.search.filters

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import me.tylerbwong.stack.data.model.SearchPayload

class FilterViewModel @ViewModelInject constructor() : ViewModel() {

    internal var updatePayloadListener: UpdatePayloadListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }
    internal var currentPayload: SearchPayload = SearchPayload.empty()

    fun applyFilters() {
        updatePayloadListener?.invoke(currentPayload)
    }

    fun clearFilters() {
        currentPayload = SearchPayload.empty()
        applyFilters()
    }
}
