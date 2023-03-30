package me.tylerbwong.stack.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.databinding.BookmarksFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeItem
import me.tylerbwong.stack.ui.home.HomeItemDiffCallback
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class BookmarksFragment : BaseFragment<BookmarksFragmentBinding>(
    BookmarksFragmentBinding::inflate
) {

    private val viewModel by viewModels<BookmarksViewModel>()
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
            adapter.submitList(null)
            viewModel.fetchBookmarks()
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
                    viewModel.fetchBookmarks()
                }
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.bookmarks.observe(viewLifecycleOwner, ::updateContent)

        binding.bookmarksRecycler.apply {
            adapter = this@BookmarksFragment.adapter
            layoutManager = LinearLayoutManager(context)
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    ViewHolderItemDecoration(
                        spacing = context.resources.getDimensionPixelSize(
                            R.dimen.item_spacing_question_detail
                        ),
                        applicableViewTypes = listOf(QuestionItem::class.java.name.hashCode()),
                    )
                )
            }
            applyInsetter {
                type(ime = true, statusBars = true, navigationBars = true) {
                    padding(bottom = true)
                }
            }
        }

        binding.refreshLayout.setOnRefreshListener { viewModel.fetchBookmarks() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchBookmarks()
    }

    private fun updateContent(drafts: List<Question>) {
        val homeItems: List<HomeItem> = listOf(
            HeaderItem(
                getString(R.string.bookmarks),
                if (drafts.isEmpty()) {
                    getString(R.string.nothing_here)
                } else {
                    null
                }
            )
        )

        adapter.submitList(homeItems + drafts.map { QuestionItem(it) })
    }
}
