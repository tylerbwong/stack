package me.tylerbwong.stack.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.databinding.FragmentHomeBinding
import me.tylerbwong.stack.ui.ApplicationWrapper
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
import javax.inject.Inject

class SearchFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    @Inject
    lateinit var viewModelFactory: SearchViewModelFactory

    private val viewModel by viewModels<SearchViewModel> { viewModelFactory }

    private val adapter = DynamicListAdapter(HomeItemDiffCallback)
    private val persistentItems: List<HomeItem>
        get() = listOf(
            HeaderItem(getString(R.string.search)),
            SearchInputItem(viewModel.searchPayload) { payload -> viewModel.search(payload) },
            FilterInputItem(viewModel.searchPayload) { payload -> viewModel.search(payload) }
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.stackComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.apply {
            adapter = this@SearchFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.siteLiveData.observe(viewLifecycleOwner) {
            viewModel.search(SearchPayload.empty())
        }

        viewModel.refreshing.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = it
        }

        viewModel.searchResults.observe(viewLifecycleOwner) {
            adapter.submitList(persistentItems + it.map { question -> QuestionItem(question) })
        }

        viewModel.emptySearchData.observe(viewLifecycleOwner) { data ->
            adapter.submitList(
                persistentItems + listOf(
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
}
