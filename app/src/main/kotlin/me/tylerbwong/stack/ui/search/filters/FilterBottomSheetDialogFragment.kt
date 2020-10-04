package me.tylerbwong.stack.ui.search.filters

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.platform.setContent
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.databinding.FiltersLayoutBinding

typealias UpdatePayloadListener = (SearchPayload) -> Unit

@AndroidEntryPoint
class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var updatePayloadListener: UpdatePayloadListener? = null
    private var searchPayload: SearchPayload = SearchPayload.empty()

    private lateinit var binding: FiltersLayoutBinding

    private val viewModel by viewModels<FilterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.updatePayloadListener = updatePayloadListener
        viewModel.currentPayload = searchPayload
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FiltersLayoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.content.setContent(Recomposer.current()) {
            FiltersLayout(::dismissAllowingStateLoss)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
        return dialog
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            searchPayload: SearchPayload,
            updatePayloadListener: UpdatePayloadListener
        ) {
            val fragment = FilterBottomSheetDialogFragment()
                .apply {
                    this.searchPayload = searchPayload
                    this.updatePayloadListener = updatePayloadListener
                }
            fragment.show(fragmentManager, FilterBottomSheetDialogFragment::class.java.simpleName)
        }
    }
}
