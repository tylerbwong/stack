package me.tylerbwong.stack.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.ACTIVITY
import me.tylerbwong.stack.api.model.CREATION
import me.tylerbwong.stack.api.model.RELEVANCE
import me.tylerbwong.stack.api.model.VOTES
import me.tylerbwong.stack.api.model.sortResourceId
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.databinding.HomeFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.home.FilterInputItem
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeItem
import me.tylerbwong.stack.ui.home.HomeItemDiffCallback
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.home.SearchHistoryItem
import me.tylerbwong.stack.ui.home.SearchInputItem
import me.tylerbwong.stack.ui.home.SectionHeaderItem
import me.tylerbwong.stack.ui.home.TagsItem
import me.tylerbwong.stack.api.R as ApiR

@AndroidEntryPoint
class SearchFragment : BaseFragment<HomeFragmentBinding>(
    HomeFragmentBinding::inflate
), PopupMenu.OnMenuItemClickListener {

    private val viewModel by viewModels<SearchViewModel>()

    private val adapter = DynamicListAdapter(HomeItemDiffCallback)
    private val persistentItems: List<HomeItem>
        get() = listOf(
            SearchInputItem(viewModel.searchPayload) { payload -> viewModel.search(payload) },
            FilterInputItem(viewModel.searchPayload) { payload -> viewModel.search(payload) }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            adapter = this@SearchFragment.adapter
            layoutManager = LinearLayoutManager(context)
            applyInsetter {
                type(ime = true, statusBars = true, navigationBars = true) {
                    padding(bottom = true)
                }
            }
        }

        viewModel.siteLiveData.observe(viewLifecycleOwner) {
            viewModel.search(SearchPayload.empty())
        }

        viewModel.refreshing.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = it
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { data ->
            val headerItem = listOf(
                HeaderItem(getString(R.string.search), getString(data.sort.sortResourceId))
            )
            adapter.submitList(
                headerItem + persistentItems + data.questions.map { question ->
                    QuestionItem(question)
                }
            )
        }

        viewModel.emptySearchData.observe(viewLifecycleOwner) { data ->
            val headerItem = listOf(
                HeaderItem(getString(R.string.search), getString(data.sort.sortResourceId))
            )
            adapter.submitList(
                headerItem + persistentItems + listOf(
                    SectionHeaderItem(getString(R.string.popular_tags)),
                    TagsItem(data.tags)
                ) + if (data.searchHistory.isNotEmpty()) {
                    listOf(
                        SectionHeaderItem(getString(R.string.past_searches))
                    ) + data.searchHistory.map {
                        SearchHistoryItem(it) { payload -> viewModel.search(payload) }
                    }
                } else {
                    emptyList()
                }
            )
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.search()
        }

        viewModel.search()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sort_item, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort -> {
                activity?.let { activity ->
                    PopupMenu(activity, activity.findViewById(R.id.sort)).also {
                        it.menu.apply {
                            add(0, R.id.activity, 0, ApiR.string.activity)
                            add(0, R.id.creation, 0, ApiR.string.creation)
                            add(0, R.id.votes, 0, ApiR.string.votes)
                            add(0, ApiR.string.relevance, 0, ApiR.string.relevance)
                        }
                        it.setOnMenuItemClickListener(this)
                        it.show()
                    }
                }
            }
        }
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val sort = when (item.itemId) {
            R.id.creation -> CREATION
            R.id.activity -> ACTIVITY
            R.id.votes -> VOTES
            ApiR.string.relevance -> RELEVANCE
            else -> RELEVANCE
        }
        viewModel.search(sort = sort)
        return true
    }
}
