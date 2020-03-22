package me.tylerbwong.stack.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeAdapter
import me.tylerbwong.stack.ui.home.TagsItem

class SearchFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by viewModels<SearchViewModel>()

    private val adapter = HomeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        refreshLayout.isEnabled = false
        recyclerView.apply {
            adapter = this@SearchFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.tags.observe(viewLifecycleOwner) {
            val homeItems = listOf(
                HeaderItem(getString(R.string.search)),
                TagsItem(it)
            )
            adapter.submitList(homeItems)
        }

        viewModel.fetchTags()
    }
}
