package me.tylerbwong.stack.ui.questions.detail

import android.animation.AnimatorInflater
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.ActivityQuestionDetailBinding
import me.tylerbwong.stack.ui.ApplicationWrapper
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.hideKeyboard
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import javax.inject.Inject

class QuestionDetailActivity : BaseActivity<ActivityQuestionDetailBinding>(
    ActivityQuestionDetailBinding::inflate
) {

    @Inject
    lateinit var viewModelFactory: QuestionDetailMainViewModelFactory

    private val viewModel by viewModels<QuestionDetailMainViewModel> { viewModelFactory }
    private lateinit var adapter: QuestionDetailPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.stackComponent.inject(this)
        setSupportActionBar(binding.toolbar)
        setTitle("")

        if (viewModel.questionId == -1) {
            viewModel.questionId = intent.getIntExtra(QUESTION_ID, -1)
        }

        viewModel.canAnswerQuestion.observe(this) {
            binding.viewPager.offscreenPageLimit = if (it) {
                2
            } else {
                1
            }
            toggleAnswerButtonVisibility(isVisible = it && !viewModel.isInAnswerMode)
        }

        binding.postAnswerButton.setThrottledOnClickListener {
            toggleAnswerMode(isInAnswerMode = true)
        }

        adapter = QuestionDetailPagerAdapter(this, viewModel.questionId)
        binding.viewPager.adapter = adapter
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                // no-op
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                // no-op
            }

            override fun onPageSelected(position: Int) {
                binding.appBar.stateListAnimator = AnimatorInflater.loadStateListAnimator(
                    this@QuestionDetailActivity,
                    if (position == 0) {
                        R.animator.app_bar_elevation
                    } else {
                        R.animator.app_bar_no_elevation
                    }
                )
            }
        })
        toggleAnswerMode(isInAnswerMode = intent.getBooleanExtra(IS_IN_ANSWER_MODE, false))
    }

    override fun applyFullscreenWindowInsets() {
        super.applyFullscreenWindowInsets()
        binding.postAnswerButton.doOnApplyWindowInsets { view, insets, initialState ->
            (view.layoutParams as? ViewGroup.MarginLayoutParams)?.let {
                view.layoutParams = it.apply {
                    setMargins(
                        leftMargin,
                        topMargin,
                        rightMargin,
                        initialState.margins.bottom + insets.systemWindowInsetBottom
                    )
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (viewModel.isInAnswerMode) {
            if (viewModel.hasContent) {
                MaterialAlertDialogBuilder(this)
                    .setBackground(ContextCompat.getDrawable(this, R.drawable.default_dialog_bg))
                    .setTitle(R.string.discard_answer)
                    .setPositiveButton(R.string.discard) { _, _ ->
                        toggleAnswerMode(isInAnswerMode = false)
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                    .create()
                    .show()
            } else {
                toggleAnswerMode(isInAnswerMode = false)
            }
        } else {
            super.onBackPressed()
        }
    }

    internal fun setTitle(title: String) {
        // Save the title from other sources to use on config changes when not in answer mode
        viewModel.title = title
        if (!viewModel.isInAnswerMode) {
            supportActionBar?.title = title
        }
    }

    internal fun extendAnswerButton() = binding.postAnswerButton.extend()

    internal fun shrinkAnswerButton() = binding.postAnswerButton.shrink()

    internal fun toggleAnswerMode(isInAnswerMode: Boolean) {
        viewModel.isInAnswerMode = isInAnswerMode
        binding.viewPager.apply {
            currentItem = if (isInAnswerMode) {
                1
            } else {
                0
            }
            isUserInputEnabled = isInAnswerMode
        }
        toggleAnswerButtonVisibility(isVisible = !isInAnswerMode)
        if (!isInAnswerMode) {
            binding.viewPager.hideKeyboard()
            viewModel.clearFields()
        }
        supportActionBar?.apply {
            if (isInAnswerMode) {
                setHomeAsUpIndicator(R.drawable.ic_close)
                setTitle(R.string.post_answer)
            } else {
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
                title = viewModel.title
            }
        }
    }

    private fun toggleAnswerButtonVisibility(isVisible: Boolean) = if (isVisible) {
        binding.postAnswerButton.show()
    } else {
        binding.postAnswerButton.hide()
    }

    companion object {
        internal const val QUESTION_ID = "id"
        internal const val IS_IN_ANSWER_MODE = "is_in_answer_mode"

        fun makeIntent(
            context: Context,
            id: Int,
            isInAnswerMode: Boolean = false
        ) = Intent(context, QuestionDetailActivity::class.java)
            .putExtra(QUESTION_ID, id)
            .putExtra(IS_IN_ANSWER_MODE, isInAnswerMode)

        fun startActivity(context: Context, id: Int) {
            context.startActivity(makeIntent(context, id))
        }
    }
}
