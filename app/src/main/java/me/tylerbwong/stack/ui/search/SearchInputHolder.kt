package me.tylerbwong.stack.ui.search

import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.search_input_holder.*
import me.tylerbwong.stack.ui.home.SearchInputItem

class SearchInputHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: SearchInputItem) {
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

    private fun bindQuery(query: String) {
        searchEditText.setText(query)
        if (query.isNotEmpty()) {
            searchEditText.setSelection(query.length)
        }
    }
}
