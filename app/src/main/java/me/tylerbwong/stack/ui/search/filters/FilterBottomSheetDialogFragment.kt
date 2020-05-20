package me.tylerbwong.stack.ui.search.filters

import android.app.Dialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.ApplicationWrapper
import javax.inject.Inject

typealias UpdatePayloadListener = (SearchPayload.Standard) -> Unit

class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var updatePayload: UpdatePayloadListener? = null

    @Inject
    lateinit var viewModelFactory: FilterViewModelFactory

    private val viewModel by viewModels<FilterViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.uiComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.filters_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val bottomSheet = dialog.findViewById<ViewGroup>(
            com.google.android.material.R.id.design_bottom_sheet
        )
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).apply {
                isGestureInsetBottomIgnored = true
            }
        }

        // Prevent peeking when in landscape to avoid only showing top of bottom sheet
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            dialog.setOnShowListener {
                bottomSheet?.let {
                    with(BottomSheetBehavior.from(bottomSheet)) {
                        peekHeight = bottomSheet.height
                        state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        } else {
            dialog.setOnShowListener(null)
        }
        return dialog
    }

    companion object {
        private const val SEARCH_PAYLOAD = "search_payload"

        fun show(
            fragmentManager: FragmentManager,
            searchPayload: SearchPayload.Standard?,
            updatePayload: UpdatePayloadListener
        ) {
            val fragment = FilterBottomSheetDialogFragment()
                .apply {
                    arguments = Bundle().apply {
                        putParcelable(SEARCH_PAYLOAD, searchPayload)
                    }
                    this.updatePayload = updatePayload
                }
            fragment.show(fragmentManager, FilterBottomSheetDialogFragment::class.java.simpleName)
        }
    }
}
