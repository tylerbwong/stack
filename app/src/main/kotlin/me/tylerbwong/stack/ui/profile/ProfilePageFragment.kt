package me.tylerbwong.stack.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.ProfilePageFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.home.HomeItemDiffCallback
import me.tylerbwong.stack.ui.profile.ProfileActivity.Companion.USER_ID
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.serializable
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class ProfilePageFragment : BaseFragment<ProfilePageFragmentBinding>(
    ProfilePageFragmentBinding::inflate
) {
    private val viewModel by viewModels<ProfileViewModel>()
    private val adapter = DynamicListAdapter(HomeItemDiffCallback)
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.userId = arguments?.getInt(USER_ID)
        viewModel.currentPage = arguments?.serializable(PAGE_EXTRA)
            ?: ProfileViewModel.ProfilePage.QUESTIONS
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.refreshing.observe(viewLifecycleOwner) {
            binding.refreshLayout.isRefreshing = it
        }
        viewModel.contentFilterUpdated.observe(viewLifecycleOwner) {
            if (viewModel.userId !in it.filteredUserIds) {
                viewModel.fetchProfileData()
            }
        }
        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.snackbar.observe(viewLifecycleOwner) {
            if (it != null) {
                snackbar =
                    binding.refreshLayout.showSnackbar(R.string.network_error, R.string.retry) {
                        viewModel.fetchUserData()
                    }
            } else {
                snackbar?.dismiss()
            }
        }
        binding.recyclerView.apply {
            adapter = this@ProfilePageFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    ViewHolderItemDecoration(
                        spacing = context.resources.getDimensionPixelSize(
                            R.dimen.item_spacing_question_detail
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
        binding.refreshLayout.setOnRefreshListener { viewModel.fetchProfileData() }
        viewModel.fetchProfileData()
    }

    companion object {
        private const val PAGE_EXTRA = "page"
        fun newInstance(
            userId: Int,
            page: ProfileViewModel.ProfilePage,
        ) = ProfilePageFragment().apply {
            arguments = Bundle().apply {
                putInt(USER_ID, userId)
                putSerializable(PAGE_EXTRA, page)
            }
        }
    }
}
