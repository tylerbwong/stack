package me.tylerbwong.stack.ui.drafts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.ui.home.AnswerDraftItem
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeAdapter
import me.tylerbwong.stack.ui.home.HomeItem
import me.tylerbwong.stack.ui.utils.showSnackbar

class DraftsFragment : Fragment(R.layout.fragment_home) {
    private val viewModel by viewModels<DraftsViewModel>()
    private val adapter = HomeAdapter()
    private var snackbar: Snackbar? = null

    private val bottomNav by lazy { activity?.findViewById<View>(R.id.bottomNav) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.refreshing.observe(this) {
            refreshLayout.isRefreshing = it
        }
        viewModel.snackbar.observe(this) {
            if (it != null) {
                snackbar = bottomNav?.showSnackbar(
                    R.string.network_error,
                    R.string.retry,
                    shouldAnchorView = true
                ) {
                    viewModel.fetchDrafts()
                }
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.drafts.observe(this, ::updateContent)

        recyclerView.apply {
            adapter = this@DraftsFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        refreshLayout.setOnRefreshListener {
            viewModel.fetchDrafts()
        }

        viewModel.fetchDrafts()
    }

    private fun updateContent(drafts: List<AnswerDraft>) {
        val homeItems: List<HomeItem> = listOf(
            HeaderItem(getString(R.string.drafts))
        )

        adapter.submitList(homeItems + drafts.map { AnswerDraftItem(it) })
    }
}
