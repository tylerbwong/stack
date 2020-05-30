package me.tylerbwong.stack.ui.search

import android.view.View
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import androidx.recyclerview.widget.RecyclerView
import me.tylerbwong.stack.databinding.SearchInputHolderBinding
import me.tylerbwong.stack.ui.home.SearchInputItem

class SearchInputHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {

    private val binding = SearchInputHolderBinding.bind(itemView)

    fun bind(item: SearchInputItem) {
        val payload = item.searchPayload
        bindQuery(payload.query)
        binding.searchEditText.setOnEditorActionListener { view, actionId, _ ->
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
        binding.searchEditText.setText(query)
        if (query.isNotEmpty()) {
            binding.searchEditText.setSelection(query.length)
        }
    }
}
