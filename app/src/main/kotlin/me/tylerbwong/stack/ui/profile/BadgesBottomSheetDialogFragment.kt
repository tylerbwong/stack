package me.tylerbwong.stack.ui.profile

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
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.BRONZE
import me.tylerbwong.stack.api.model.GOLD
import me.tylerbwong.stack.api.model.SILVER
import me.tylerbwong.stack.databinding.BadgesFragmentBinding
import me.tylerbwong.stack.ui.utils.createChip
import me.tylerbwong.stack.ui.utils.toHtml

@AndroidEntryPoint
class BadgesBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val viewModel by viewModels<BadgesViewModel>()
    private lateinit var binding: BadgesFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.userId = arguments?.getInt(USER_ID) ?: -1
        if (viewModel.userId == -1) {
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BadgesFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.header.title.text = getString(R.string.badges)
        viewModel.refreshing.observe(viewLifecycleOwner) { isRefreshing ->
            if (isRefreshing) {
                binding.loadingIndicator.show()
            }
        }
        viewModel.badges.observe(viewLifecycleOwner) { badges ->
            binding.loadingIndicator.hide()
            binding.header.subtitle.text = getString(R.string.item_count, badges.size)
            binding.badgesLayout.removeAllViews()
            val chips = with(binding.badgesLayout.context) {
                badges.map {
                    val strokeColorRes = when (it.rank) {
                        GOLD -> R.color.goldBadgeColorDark
                        SILVER -> R.color.silverBadgeColorDark
                        BRONZE -> R.color.bronzeBadgeColorDark
                        else -> R.color.secondaryTextColor
                    }
                    createChip(
                        it.name.toHtml().toString(),
                        backgroundColorRes = R.color.dialogBackgroundColor,
                        strokeColorRes = strokeColorRes,
                        strokeWidthRes = R.dimen.badge_chip_stroke_width
                    )
                }
            }
            chips.forEach { binding.badgesLayout.addView(it) }
        }

        viewModel.fetchBadges()
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
        private const val USER_ID = "user_id"

        fun show(fragmentManager: FragmentManager, userId: Int) {
            val fragment = BadgesBottomSheetDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(USER_ID, userId)
                }
            }
            fragment.show(fragmentManager, BadgesBottomSheetDialogFragment::class.java.simpleName)
        }
    }
}
