package me.tylerbwong.stack.ui.drafts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applySystemWindowInsetsToPadding
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.databinding.HomeFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.home.AnswerDraftItem
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeItem
import me.tylerbwong.stack.ui.home.HomeItemDiffCallback
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class DraftsFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    private val viewModel by viewModels<DraftsViewModel>()
    private val adapter = DynamicListAdapter(HomeItemDiffCallback)
    private var snackbar: Snackbar? = null

    private val bottomNav by lazy { activity?.findViewById<View>(R.id.bottomNav) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.siteLiveData.observe(viewLifecycleOwner) {
            viewModel.fetchDrafts()
        }
        viewModel.refreshing.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = it
        }
        viewModel.snackbar.observe(viewLifecycleOwner) {
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
        viewModel.answerDrafts.observe(viewLifecycleOwner, ::updateContent)

        binding.recyclerView.apply {
            adapter = this@DraftsFragment.adapter
            layoutManager = LinearLayoutManager(context)
            applySystemWindowInsetsToPadding(bottom = true)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchDrafts()
        }

        viewModel.fetchDrafts()
    }

    private fun updateContent(drafts: List<AnswerDraft>) {
        val homeItems: List<HomeItem> = listOf(
            HeaderItem(
                getString(R.string.drafts),
                if (drafts.isEmpty()) {
                    getString(R.string.nothing_here)
                } else {
                    null
                }
            )
        )

        adapter.submitList(homeItems + drafts.map { AnswerDraftItem(it) })
    }
}
