package me.tylerbwong.stack.ui.search

import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.basic_search_input_holder.*
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.home.BasicSearchInputItem
import me.tylerbwong.stack.ui.utils.addThrottledTextChangedListener

class BasicSearchInputHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    private var textWatcher: TextWatcher? = null

    fun bind(item: BasicSearchInputItem) {
        searchEditText.removeTextChangedListener(textWatcher)

        when (val payload = item.searchPayload) {
            is SearchPayload.Advanced -> bindQuery(payload.query)
            is SearchPayload.Basic -> bindQuery(payload.query)
            is SearchPayload.Empty -> bindQuery()
        }

        textWatcher = searchEditText.addThrottledTextChangedListener { result ->
            item.onPayloadReceived(
                if (result.isNotBlank()) {
                    SearchPayload.Basic(query = result)
                } else {
                    SearchPayload.Empty
                }
            )
        }
    }

    private fun bindQuery(query: String = "") {
        searchEditText.setText(query)
        searchEditText.setSelection(query.length)
    }
}
