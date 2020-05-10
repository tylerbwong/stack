package me.tylerbwong.stack.ui.drafts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.AnswerDraft
import me.tylerbwong.stack.ui.ApplicationWrapper
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.home.AnswerDraftItem
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeAdapter
import me.tylerbwong.stack.ui.home.HomeItem
import me.tylerbwong.stack.ui.utils.showSnackbar
import javax.inject.Inject

class DraftsFragment : BaseFragment(R.layout.fragment_home) {

    @Inject
    lateinit var viewModelFactory: DraftsViewModelFactory

    private val viewModel by viewModels<DraftsViewModel> { viewModelFactory }
    private val adapter = HomeAdapter()
    private var snackbar: Snackbar? = null

    private val bottomNav by lazy { activity?.findViewById<View>(R.id.bottomNav) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.uiComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.refreshing.observe(viewLifecycleOwner) {
            refreshLayout.isRefreshing = it
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
        viewModel.drafts.observe(viewLifecycleOwner, ::updateContent)

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
