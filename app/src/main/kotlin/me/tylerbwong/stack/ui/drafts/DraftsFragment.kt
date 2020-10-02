package me.tylerbwong.stack.ui.drafts

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.platform.setContent
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.HomeComposeFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class DraftsFragment : BaseFragment<HomeComposeFragmentBinding>(
    HomeComposeFragmentBinding::inflate
) {
    private val viewModel by viewModels<DraftsViewModel>()
    private var snackbar: Snackbar? = null

    private val bottomNav by lazy { activity?.findViewById<View>(R.id.bottomNav) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.content.setContent(Recomposer.current()) {
            Drafts()
        }

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

        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchDrafts()
        }

        viewModel.fetchDrafts()
    }
}
