package me.tylerbwong.stack.ui.search

import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.search_input_holder.*
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.home.SearchInputItem

class SearchInputHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private var textWatcher: TextWatcher? = null

    fun bind(item: SearchInputItem) {
        searchEditText.removeTextChangedListener(textWatcher)

        when (item.searchPayload) {
            is SearchPayload.Advanced -> bindAdvancedPayload(item.searchPayload)
            is SearchPayload.Basic -> bindBasicPayload(item.searchPayload)
            is SearchPayload.Empty -> bindEmptyPayload()
        }

        textWatcher = searchEditText.addTextChangedListener {
            val result = it?.toString()?.trim()
            item.onPayloadReceived(
                if (!result.isNullOrBlank()) {
                    SearchPayload.Basic(query = result)
                } else {
                    SearchPayload.Empty
                }
            )
        }
    }

    private fun bindAdvancedPayload(advancedPayload: SearchPayload.Advanced) {
        val (query) = advancedPayload
        bindBasicPayload(SearchPayload.Basic(query))
    }

    private fun bindBasicPayload(basicPayload: SearchPayload.Basic) {
        val query = basicPayload.query
        searchEditText.setText(query)
        searchEditText.setSelection(query.length)
    }

    private fun bindEmptyPayload() {
        searchEditText.text = null
    }
}
