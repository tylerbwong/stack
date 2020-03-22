package me.tylerbwong.stack.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.ACTIVITY
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.HOT
import me.tylerbwong.stack.data.model.MONTH
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.VOTES
import me.tylerbwong.stack.data.model.WEEK
import me.tylerbwong.stack.data.model.sortResourceId
import me.tylerbwong.stack.ui.utils.showSnackbar

class HomeFragment : Fragment(R.layout.fragment_home), PopupMenu.OnMenuItemClickListener {
    private val viewModel: HomeViewModel by viewModels()
    private val adapter = HomeAdapter()
    private var snackbar: Snackbar? = null

    private val bottomNav by lazy { activity?.findViewById<View>(R.id.bottomNav) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.refreshing.observe(viewLifecycleOwner) {
            refreshLayout.isRefreshing = it
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

        recyclerView.apply {
            adapter = this@HomeFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }

        refreshLayout.setOnRefreshListener {
            viewModel.fetchQuestions()
        }

        viewModel.fetchQuestions()
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val sort = when (item?.itemId) {
            R.id.creation -> CREATION
            R.id.activity -> ACTIVITY
            R.id.votes -> VOTES
            R.id.hot -> HOT
            R.id.week -> WEEK
            R.id.month -> MONTH
            else -> CREATION
        }
        viewModel.getQuestions(sort)
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
