package me.tylerbwong.stack.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeAdapter
import me.tylerbwong.stack.ui.home.HomeItem

class SearchFragment : Fragment(R.layout.fragment_home) {

    private val adapter = HomeAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        refreshLayout.isEnabled = false
        recyclerView.apply {
            adapter = this@SearchFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        val homeItems: List<HomeItem> = listOf(
            HeaderItem(getString(R.string.search), getString(R.string.nothing_here))
        )

        adapter.submitList(homeItems)
    }
}
