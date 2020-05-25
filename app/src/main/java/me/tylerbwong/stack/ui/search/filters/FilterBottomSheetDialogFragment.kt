package me.tylerbwong.stack.ui.search.filters

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.Slider
import kotlinx.android.synthetic.main.filters_layout.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.ui.ApplicationWrapper
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import javax.inject.Inject

typealias UpdatePayloadListener = (SearchPayload) -> Unit

class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var updatePayloadListener: UpdatePayloadListener? = null

    @Inject
    lateinit var viewModelFactory: FilterViewModelFactory

    private val viewModel by viewModels<FilterViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.stackComponent.inject(this)
        viewModel.updatePayloadListener = updatePayloadListener
        viewModel.currentPayload = arguments?.getParcelable(SEARCH_PAYLOAD)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.AppTheme_Base)
        return inflater.cloneInContext(contextThemeWrapper)
            .inflate(R.layout.filters_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.currentPayload?.let { payload ->
            hasAcceptedAnswerSwitch.isChecked = payload.isAccepted ?: false
            isClosedSwitch.isChecked = payload.isClosed ?: false
            val minAnswers = payload.minNumAnswers ?: 0
            minAnswersTitle.text = requireContext().resources
                .getQuantityString(R.plurals.has_min_answers, minAnswers, minAnswers)
            minAnswersSlider.value = minAnswers.toFloat()
            titleContainsEditText.setText(payload.titleContains)
            bodyContainsEditText.setText(payload.bodyContains)
            tagsEditText.setText(payload.tags?.joinToString(","))
        }

        hasAcceptedAnswerSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.hasAcceptedAnswer = isChecked
        }
        isClosedSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.isClosed = isChecked
        }
        minAnswersSlider.addOnChangeListener { _, value, _ ->
            val minAnswers = value.toInt()
            minAnswersTitle.text = requireContext().resources
                .getQuantityString(R.plurals.has_min_answers, minAnswers, minAnswers)
        }
        minAnswersSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // No-op
            }

            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.minimumAnswers = slider.value.toInt()
            }
        })
        titleContainsEditText.addTextChangedListener {
            viewModel.titleContains = it?.toString()
        }
        bodyContainsEditText.addTextChangedListener {
            viewModel.bodyContains = it?.toString()
        }
        tagsEditText.addTextChangedListener {
            viewModel.tags = it?.toString()
        }
        applyFiltersButton.setThrottledOnClickListener {
            viewModel.applyFilters()
            dismissAllowingStateLoss()
        }
        clearFiltersButton.setThrottledOnClickListener {
            viewModel.clearFilters()
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val bottomSheet = dialog.findViewById<ViewGroup>(
            com.google.android.material.R.id.design_bottom_sheet
        )
        dialog.setOnShowListener {
            bottomSheet?.let {
                with(BottomSheetBehavior.from(bottomSheet)) {
                    peekHeight = bottomSheet.height
                    state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
        return dialog
    }

    companion object {
        private const val SEARCH_PAYLOAD = "search_payload"

        fun show(
            fragmentManager: FragmentManager,
            searchPayload: SearchPayload?,
            updatePayloadListener: UpdatePayloadListener
        ) {
            val fragment = FilterBottomSheetDialogFragment()
                .apply {
                    arguments = Bundle().apply {
                        putParcelable(SEARCH_PAYLOAD, searchPayload)
                    }
                    this.updatePayloadListener = updatePayloadListener
                }
            fragment.show(fragmentManager, FilterBottomSheetDialogFragment::class.java.simpleName)
        }
    }
}
