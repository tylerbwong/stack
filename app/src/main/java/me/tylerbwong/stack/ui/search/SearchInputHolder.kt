package me.tylerbwong.stack.ui.search

import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.search_input_holder.*
import me.tylerbwong.stack.ui.home.SearchInputItem

class SearchInputHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(item: SearchInputItem) {
        searchEditText.addTextChangedListener {
            it?.toString()?.let { result -> item.onQueryReceived(result) }
        }
    }
}
