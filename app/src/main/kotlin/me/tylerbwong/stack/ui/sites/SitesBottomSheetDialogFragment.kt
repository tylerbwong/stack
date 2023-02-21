package me.tylerbwong.stack.ui.sites

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.databinding.SitesFragmentBinding
import me.tylerbwong.stack.ui.settings.sites.SitesLayout
import me.tylerbwong.stack.ui.settings.sites.SitesViewModel

@AndroidEntryPoint
class SitesBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private val viewModel by viewModels<SitesViewModel>()
    private lateinit var binding: SitesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SitesFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.composeContent.setContent {
            SitesLayout(changeSite = ::changeSite)
        }
        viewModel.fetchSites()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        return dialog
    }

    private fun changeSite(siteParameter: String) {
        viewModel.changeSite(siteParameter)
        dismissAllowingStateLoss()
    }

    companion object {
        fun show(fragmentManager: FragmentManager) {
            val fragment = SitesBottomSheetDialogFragment()
            fragment.show(fragmentManager, SitesBottomSheetDialogFragment::class.java.simpleName)
        }
    }
}
