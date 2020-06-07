package me.tylerbwong.stack.ui.search

import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import me.tylerbwong.adapter.viewbinding.DynamicViewBindingHolder
import me.tylerbwong.stack.databinding.SearchInputHolderBinding
import me.tylerbwong.stack.ui.home.SearchInputItem

class SearchInputHolder(
    container: ViewGroup
) : DynamicViewBindingHolder<SearchInputItem, SearchInputHolderBinding>(
    container,
    SearchInputHolderBinding::inflate
) {
    override fun SearchInputHolderBinding.bind(item: SearchInputItem) {
        val payload = item.searchPayload
        bindQuery(payload.query)
        searchEditText.setOnEditorActionListener { view, actionId, _ ->
            when (actionId) {
                IME_ACTION_SEARCH -> {
                    val result = view.text.toString()
                    item.onPayloadReceived(payload.copy(query = result))
                    true
                }
                else -> false
            }
        }
    }

    private fun SearchInputHolderBinding.bindQuery(query: String) {
        searchEditText.setText(query)
        if (query.isNotEmpty()) {
            searchEditText.setSelection(query.length)
        }
    }
}
