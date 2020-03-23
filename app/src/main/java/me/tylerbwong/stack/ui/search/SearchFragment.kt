package me.tylerbwong.stack.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.home.AdvancedSearchInputItem
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeAdapter
import me.tylerbwong.stack.ui.home.HomeItem
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.home.BasicSearchInputItem
import me.tylerbwong.stack.ui.home.SectionHeaderItem
import me.tylerbwong.stack.ui.home.TagsItem

class SearchFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by viewModels<SearchViewModel>()

    private val adapter = HomeAdapter()
    private val persistentItems: List<HomeItem>
        get() = listOf(
            HeaderItem(getString(R.string.search)),
            BasicSearchInputItem(viewModel.searchPayload) { payload -> viewModel.search(payload) },
            AdvancedSearchInputItem(viewModel.searchPayload) { payload -> viewModel.search(payload) }
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.apply {
            adapter = this@SearchFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.refreshing.observe(viewLifecycleOwner) {
            refreshLayout.isRefreshing = it
        }

        viewModel.searchResults.observe(viewLifecycleOwner) {
            adapter.submitList(persistentItems + it.map { question -> QuestionItem(question) })
        }

        viewModel.tags.observe(viewLifecycleOwner) {
            adapter.submitList(
                persistentItems + listOf(
                    SectionHeaderItem(getString(R.string.popular_tags)),
                    TagsItem(it)
                )
            )
        }

        refreshLayout.setOnRefreshListener {
            viewModel.search()
        }

        viewModel.search()
    }
}
