package me.tylerbwong.stack.ui.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.databinding.FragmentHomeBinding
import me.tylerbwong.stack.ui.ApplicationWrapper
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeAdapter
import me.tylerbwong.stack.ui.home.HomeItem
import me.tylerbwong.stack.ui.home.QuestionItem
import me.tylerbwong.stack.ui.utils.showSnackbar
import javax.inject.Inject

class BookmarksFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    @Inject
    lateinit var viewModelFactory: BookmarksViewModelFactory

    private val viewModel by viewModels<BookmarksViewModel> { viewModelFactory }
    private val adapter = HomeAdapter()
    private var snackbar: Snackbar? = null

    private val bottomNav by lazy { activity?.findViewById<View>(R.id.bottomNav) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.stackComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        binding.recyclerView.apply {
            adapter = this@BookmarksFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchBookmarks()
        }
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
