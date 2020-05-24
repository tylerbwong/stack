package me.tylerbwong.stack.ui.search.filters

import android.app.Dialog
import android.content.res.Configuration
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
import javax.inject.Inject

typealias UpdatePayloadListener = (SearchPayload) -> Unit

class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var updatePayload: UpdatePayloadListener? = null

    @Inject
    lateinit var viewModelFactory: FilterViewModelFactory

    private val viewModel by viewModels<FilterViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.stackComponent.inject(this)
        viewModel.currentPayload = arguments?.getParcelable(SEARCH_PAYLOAD) ?: SearchPayload("")
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
        with(viewModel.currentPayload) {
            hasAcceptedAnswerSwitch.isChecked = isAccepted ?: false
            isClosedSwitch.isChecked = isClosed ?: false
            val minAnswers = minNumAnswers ?: 0
            minAnswersTitle.text = requireContext().resources
                .getQuantityString(R.plurals.has_min_answers, minAnswers, minAnswers)
            minAnswersSlider.value = minAnswers.toFloat()
            titleContainsEditText.setText(titleContains)
            bodyContainsEditText.setText(bodyContains)
            tagsEditText.setText(tags?.joinToString(","))
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
            viewModel.titleContains = it?.toString() ?: ""
        }
        bodyContainsEditText.addTextChangedListener {
            viewModel.bodyContains = it?.toString() ?: ""
        }
        tagsEditText.addTextChangedListener {
            viewModel.tags = it?.toString() ?: ""
        }
        addFiltersButton.setOnClickListener {
            updatePayload?.invoke(viewModel.currentPayload)
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val bottomSheet = dialog.findViewById<ViewGroup>(
            com.google.android.material.R.id.design_bottom_sheet
        )
        if (bottomSheet != null) {
            BottomSheetBehavior.from(bottomSheet).apply {
                isGestureInsetBottomIgnored = true
                state = BottomSheetBehavior.STATE_EXPANDED
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
            searchPayload: SearchPayload?,
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
