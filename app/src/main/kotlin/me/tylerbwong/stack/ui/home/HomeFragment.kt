package me.tylerbwong.stack.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.ACTIVITY
import me.tylerbwong.stack.api.model.CREATION
import me.tylerbwong.stack.api.model.HOT
import me.tylerbwong.stack.api.model.MONTH
import me.tylerbwong.stack.api.model.Question
import me.tylerbwong.stack.api.model.VOTES
import me.tylerbwong.stack.api.model.WEEK
import me.tylerbwong.stack.api.model.sortResourceId
import me.tylerbwong.stack.databinding.HomeFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>(
    HomeFragmentBinding::inflate
), PopupMenu.OnMenuItemClickListener {

    private val viewModel by viewModels<HomeViewModel>()
    private val adapter = DynamicListAdapter(HomeItemDiffCallback)
    private var snackbar: Snackbar? = null

    private val bottomNav by lazy { activity?.findViewById<View>(R.id.bottomNav) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.siteLiveData.observe(viewLifecycleOwner) {
            viewModel.fetchQuestions()
        }
        viewModel.refreshing.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = it
        }
        viewModel.snackbar.observe(viewLifecycleOwner) {
            if (it != null) {
                bottomNav?.post {
                    snackbar = bottomNav?.showSnackbar(
                        R.string.network_error,
                        R.string.retry,
                        shouldAnchorView = true
                    ) {
                        viewModel.fetchQuestions()
                    }
                }
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.questions.observe(viewLifecycleOwner, ::updateContent)

        binding.recyclerView.apply {
            adapter = this@HomeFragment.adapter
            layoutManager = LinearLayoutManager(context)
            applyInsetter {
                type(ime = true, statusBars = true, navigationBars = true) {
                    padding(bottom = true)
                }
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchQuestions()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sort_item, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort -> {
                activity?.let { activity ->
                    PopupMenu(activity, activity.findViewById(R.id.sort)).also {
                        it.inflate(R.menu.menu_sort)
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
            R.id.hot -> HOT
            R.id.week -> WEEK
            R.id.month -> MONTH
            else -> CREATION
        }
        viewModel.fetchQuestions(sort)
        return true
    }

    private fun updateContent(questions: List<Question>) {
        val homeItems: List<HomeItem> = listOf(
            HeaderItem(
                getString(R.string.questions),
                getString(viewModel.currentSort.sortResourceId)
            )
        )

        adapter.submitList(homeItems + questions.map { QuestionItem(it) })
    }
}
