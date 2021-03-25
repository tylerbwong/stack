package me.tylerbwong.stack.ui.search.filters

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.Slider
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.SearchPayload
import me.tylerbwong.stack.databinding.FiltersLayoutBinding
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener

typealias UpdatePayloadListener = (SearchPayload) -> Unit

@AndroidEntryPoint
class FilterBottomSheetDialogFragment : BottomSheetDialogFragment(), Slider.OnChangeListener {

    private var updatePayloadListener: UpdatePayloadListener? = null
    private var searchPayload: SearchPayload? = null

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
    ): View {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.AppTheme_Base)
        binding = FiltersLayoutBinding.inflate(inflater.cloneInContext(contextThemeWrapper))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with(binding) {
            viewModel.currentPayload?.let { payload ->
                binding.composeContent.setContent {
                    FiltersLayout(
                        initialPayload = payload,
                        onUpdateFilters = { viewModel.currentPayload = it }
                    )
                }
                val minAnswers = payload.minNumAnswers ?: 0
                minAnswersTitle.text = requireContext().resources
                    .getQuantityString(R.plurals.has_min_answers, minAnswers, minAnswers)
                minAnswersSlider.value = minAnswers.toFloat()
                titleContainsEditText.setText(payload.titleContains)
                bodyContainsEditText.setText(payload.bodyContains)
                tagsEditText.setText(payload.tags?.joinToString(","))
            }

            minAnswersSlider.addOnChangeListener(this@FilterBottomSheetDialogFragment)
            minAnswersSlider.addOnSliderTouchListener(
                object : Slider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: Slider) {
                        // No-op
                    }

                    override fun onStopTrackingTouch(slider: Slider) {
                        viewModel.minimumAnswers = slider.value.toInt()
                    }
                }
            )
            titleContainsEditText.doAfterTextChanged {
                viewModel.titleContains = it?.toString()
            }
            bodyContainsEditText.doAfterTextChanged {
                viewModel.bodyContains = it?.toString()
            }
            tagsEditText.doAfterTextChanged {
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
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.behavior.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
        return dialog
    }

    override fun onValueChange(slider: Slider, value: Float, fromUser: Boolean) {
        val minAnswers = value.toInt()
        binding.minAnswersTitle.text = requireContext().resources
            .getQuantityString(R.plurals.has_min_answers, minAnswers, minAnswers)
    }

    companion object {
        fun show(
            fragmentManager: FragmentManager,
            searchPayload: SearchPayload?,
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
