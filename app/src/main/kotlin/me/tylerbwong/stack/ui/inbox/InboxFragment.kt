package me.tylerbwong.stack.ui.inbox

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
import me.tylerbwong.stack.api.model.InboxItem
import me.tylerbwong.stack.data.repository.ALL
import me.tylerbwong.stack.data.repository.UNREAD
import me.tylerbwong.stack.databinding.InboxFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.home.HeaderItem
import me.tylerbwong.stack.ui.home.HomeItem
import me.tylerbwong.stack.ui.home.HomeItemDiffCallback
import me.tylerbwong.stack.ui.home.InboxHomeItem
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class InboxFragment : BaseFragment<InboxFragmentBinding>(
    InboxFragmentBinding::inflate
), PopupMenu.OnMenuItemClickListener {

    private val viewModel by viewModels<InboxViewModel>()
    private val adapter = DynamicListAdapter(HomeItemDiffCallback)
    private var snackbar: Snackbar? = null

    private val bottomNav by lazy { activity?.findViewById<View>(R.id.bottomNav) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refreshing.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = it
        }
        binding.refreshLayout.setOnRefreshListener { viewModel.fetchInbox() }
        viewModel.snackbar.observe(viewLifecycleOwner) {
            if (it != null) {
                bottomNav?.post {
                    snackbar = bottomNav?.showSnackbar(
                        R.string.network_error,
                        R.string.retry,
                        shouldAnchorView = true
                    ) { viewModel.fetchInbox() }
                }
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.inboxItems.observe(viewLifecycleOwner, ::updateContent)

        binding.inboxRecycler.apply {
            adapter = this@InboxFragment.adapter
            layoutManager = LinearLayoutManager(context)
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    ViewHolderItemDecoration(
                        spacing = context.resources.getDimensionPixelSize(
                            R.dimen.item_spacing_question_detail
                        ),
                        applicableViewTypes = listOf(InboxHomeItem::class.java.name.hashCode()),
                    )
                )
            }
            applyInsetter {
                type(ime = true, statusBars = true, navigationBars = true) {
                    padding(bottom = true)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchInbox()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_inbox_item, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter -> {
                activity?.let { activity ->
                    PopupMenu(activity, activity.findViewById(R.id.filter)).also {
                        it.inflate(R.menu.menu_inbox)
                        it.setOnMenuItemClickListener(this)
                        it.show()
                    }
                }
            }
        }
        return true
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val filter = when (item.itemId) {
            R.id.all -> ALL
            R.id.unread -> UNREAD
            else -> ALL
        }
        viewModel.fetchInbox(filter)
        return true
    }

    private fun updateContent(inboxItems: List<InboxItem>) {
        val homeItems: List<HomeItem> = listOf(
            HeaderItem(
                getString(R.string.inbox),
                getString(R.string.unread_count, viewModel.unreadCount.value)
            )
        )

        adapter.submitList(
            homeItems + inboxItems.map {
                InboxHomeItem(it) { context, item -> viewModel.onItemClicked(context, item) }
            }
        )
    }
}
