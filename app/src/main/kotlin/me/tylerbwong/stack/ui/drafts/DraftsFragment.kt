package me.tylerbwong.stack.ui.drafts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.adapter.DynamicItem
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.HomeFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.home.AnswerDraftItem
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeItem
import me.tylerbwong.stack.ui.home.HomeItemDiffCallback
import me.tylerbwong.stack.ui.home.QuestionDraftItem
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.formatElapsedTime
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class DraftsFragment : BaseFragment<HomeFragmentBinding>(HomeFragmentBinding::inflate) {

    private val viewModel by viewModels<DraftsViewModel>()
    private val adapter = DynamicListAdapter(HomeItemDiffCallback)
    private var snackbar: Snackbar? = null

    private val bottomNav by lazy { activity?.findViewById<View>(R.id.bottomNav) }
    private val timestampProvider: (Long) -> String = { it.formatElapsedTime(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.siteLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(null)
            viewModel.fetchDrafts(timestampProvider)
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
                    viewModel.fetchDrafts(timestampProvider)
                }
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.drafts.observe(viewLifecycleOwner, ::updateContent)

        binding.recyclerView.apply {
            adapter = this@DraftsFragment.adapter
            layoutManager = LinearLayoutManager(context)
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    ViewHolderItemDecoration(
                        spacing = context.resources.getDimensionPixelSize(
                            R.dimen.item_spacing_question_detail
                        ),
                        applicableViewTypes = listOf(
                            AnswerDraftItem::class.java.name.hashCode(),
                            QuestionDraftItem::class.java.name.hashCode(),
                        ),
                    )
                )
            }
            applyInsetter {
                type(ime = true, statusBars = true, navigationBars = true) {
                    padding(bottom = true)
                }
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchDrafts(timestampProvider)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchDrafts(timestampProvider)
    }

    private fun updateContent(drafts: List<DynamicItem>) {
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

        adapter.submitList(homeItems + drafts)
    }
}
