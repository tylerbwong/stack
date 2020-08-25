package me.tylerbwong.stack.ui.settings.sites

import android.os.Bundle
import android.view.View
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.platform.setContent
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.SitesFragmentBinding
import me.tylerbwong.stack.ui.BaseFragment

@AndroidEntryPoint
class SitesComposeFragment : BaseFragment<SitesFragmentBinding>(SitesFragmentBinding::inflate) {

    private val viewModel by viewModels<SitesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            binding.composeContent.setContent(Recomposer.current()) {
                SitesLayout(
                    sitesLiveData = viewModel.sites,
                    onSiteSelected = { site ->
                        if (viewModel.isAuthenticated) {
                            MaterialAlertDialogBuilder(requireContext())
                                .setBackground(
                                    ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.default_dialog_bg
                                    )
                                )
                                .setTitle(R.string.log_out_title)
                                .setMessage(R.string.log_out_site_switch)
                                .setPositiveButton(R.string.log_out) { _, _ -> viewModel.logOut(site.parameter) }
                                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                                .create()
                                .show()
                        } else {
                            changeSite(site.parameter)
                        }
                    }
                )
            }
        }
        viewModel.fetchSites()
    }

    private fun changeSite(siteParameter: String) {
        viewModel.changeSite(siteParameter)
        requireActivity().finish()
    }
}
