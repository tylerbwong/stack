package me.tylerbwong.stack.ui.questions.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.content.ContentFilter
import me.tylerbwong.stack.data.reviewer.AppReviewer
import me.tylerbwong.stack.databinding.ActivityQuestionDetailBinding
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.hideKeyboard
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.showDialog
import me.tylerbwong.stack.ui.utils.showRegisterOnSiteDialog
import javax.inject.Inject

@AndroidEntryPoint
class QuestionDetailActivity : BaseActivity<ActivityQuestionDetailBinding>(
    ActivityQuestionDetailBinding::inflate
) {
    private val viewModel by viewModels<QuestionDetailMainViewModel>()
    private lateinit var adapter: QuestionDetailPagerAdapter
    override val isDefaultBackBehaviorEnabled = false

    @Inject
    lateinit var contentFilter: ContentFilter

    @Inject
    lateinit var appReviewer: AppReviewer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        if (viewModel.questionId == -1) {
            viewModel.questionId = intent.getIntExtra(QUESTION_ID, -1)
            checkContentFilter()
        }

        if (viewModel.answerId == -1) {
            viewModel.answerId = intent.getIntExtra(ANSWER_ID, -1)
        }

        if (viewModel.commentId == -1) {
            viewModel.commentId = intent.getIntExtra(COMMENT_ID, -1)
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
            if (viewModel.isAuthenticated && viewModel.user.value == null) {
                viewModel.site.value?.let { site ->
                    showRegisterOnSiteDialog(
                        site = site,
                        siteUrl = viewModel.buildSiteJoinUrl(site),
                        titleResId = R.string.register_on_site_contribute,
                    )
                }
            } else {
                toggleAnswerMode(isInAnswerMode = true)
            }
        }

        adapter = QuestionDetailPagerAdapter(this, viewModel.questionId)
        binding.viewPager.adapter = adapter

        val isInAnswerMode = savedInstanceState?.getBoolean(IS_IN_ANSWER_MODE, false)
            ?: intent.getBooleanExtra(IS_IN_ANSWER_MODE, false)
        toggleAnswerMode(isInAnswerMode = isInAnswerMode)
        appReviewer.markReviewImpression()
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBack()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        checkContentFilter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_IN_ANSWER_MODE, viewModel.isInAnswerMode)
    }

    override fun applyFullscreenWindowInsets() {
        super.applyFullscreenWindowInsets()
        binding.postAnswerButton.applyInsetter {
            type(ime = true, statusBars = true, navigationBars = true) {
                margin(left = true, top = true, right = true, bottom = true)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBack(isSystemBack = false)
        }
        return super.onOptionsItemSelected(item)
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

    private fun checkContentFilter() {
        val isQuestionIdHidden = viewModel.questionId in contentFilter.filteredQuestionIds
        val isUserIdHidden = viewModel.question?.owner?.userId in contentFilter.filteredUserIds
        if (isQuestionIdHidden || isUserIdHidden) {
            Toast.makeText(this, R.string.hide_post_hidden, Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun onBack(isSystemBack: Boolean = true) {
        if (viewModel.isInAnswerMode) {
            if (viewModel.hasContent) {
                showDialog {
                    setMessage(R.string.discard_answer)
                    setPositiveButton(R.string.discard) { _, _ ->
                        toggleAnswerMode(isInAnswerMode = false)
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                }
            } else {
                toggleAnswerMode(isInAnswerMode = false)
            }
        } else {
            defaultOnBackPressed(isSystemBack = isSystemBack)
        }
    }

    companion object {
        internal const val QUESTION_ID = "question_id"
        internal const val ANSWER_ID = "answer_id"
        internal const val COMMENT_ID = "comment_id"
        internal const val IS_IN_ANSWER_MODE = "is_in_answer_mode"

        fun makeIntent(
            context: Context,
            questionId: Int,
            answerId: Int? = null,
            commentId: Int? = null,
            isInAnswerMode: Boolean = false,
            deepLinkSite: String? = null
        ) = Intent(context, QuestionDetailActivity::class.java)
            .putExtra(QUESTION_ID, questionId)
            .putExtra(ANSWER_ID, answerId)
            .putExtra(COMMENT_ID, commentId)
            .putExtra(IS_IN_ANSWER_MODE, isInAnswerMode)
            .putExtra(DEEP_LINK_SITE, deepLinkSite)

        fun startActivity(context: Context, id: Int) {
            context.startActivity(makeIntent(context, id))
        }
    }
}
