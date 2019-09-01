package me.tylerbwong.stack.ui.questions.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_question_detail.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.hideKeyboard

class QuestionDetailActivity : BaseActivity() {

    private val viewModel by viewModels<QuestionDetailMainViewModel>()
    private lateinit var adapter: QuestionDetailPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        setSupportActionBar(toolbar)
        setTitle("")

        viewModel.questionId = savedInstanceState?.getInt(QUESTION_ID)
                ?: intent.getIntExtra(QUESTION_ID, 0)

        AuthStore.isAuthenticatedLiveData.observe(this) {
            toggleAnswerButtonVisibility(isVisible = it && !viewModel.isInAnswerMode)
        }

        addAnswerButton.setOnClickListener {
            toggleAnswerMode(isInAnswerMode = true)
        }

        adapter = QuestionDetailPagerAdapter(supportFragmentManager, viewModel.questionId)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                // no-op
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // no-op
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    appBar.elevation = resources.getDimension(R.dimen.default_app_bar_elevation)
                    rootLayout.hideKeyboard()
                } else {
                    appBar.elevation = 0f
                }
            }
        })
        toggleAnswerMode(isInAnswerMode = viewModel.isInAnswerMode)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (viewModel.isInAnswerMode) {
            MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.discard_answer)
                    .setPositiveButton(R.string.discard) { _, _ ->
                        toggleAnswerMode(isInAnswerMode = false)
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                    .create()
                    .show()
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

    private fun toggleAnswerButtonVisibility(isVisible: Boolean) = if (isVisible) {
        addAnswerButton.show(false)
    } else {
        addAnswerButton.hide(false)
    }

    internal fun extendAnswerButton() = addAnswerButton.extend()

    internal fun shrinkAnswerButton() = addAnswerButton.shrink()

    private fun toggleAnswerMode(isInAnswerMode: Boolean) {
        viewModel.isInAnswerMode = isInAnswerMode
        adapter.isInAnswerMode = isInAnswerMode
        viewPager.isSwipeable = isInAnswerMode
        toggleAnswerButtonVisibility(isVisible = !isInAnswerMode)
        if (!isInAnswerMode) {
            viewPager.hideKeyboard()
        }
        supportActionBar?.apply {
            if (isInAnswerMode) {
                setHomeAsUpIndicator(R.drawable.ic_close)
                setTitle(R.string.add_answer)
            } else {
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
                title = viewModel.title
            }
        }
        viewPager.currentItem = if (isInAnswerMode) {
            1
        } else {
            0
        }
    }

    companion object {
        internal const val QUESTION_ID = "id"

        fun makeIntent(
                context: Context,
                id: Int
        ) = Intent(context, QuestionDetailActivity::class.java)
                .putExtra(QUESTION_ID, id)

        fun startActivity(
                context: Context,
                id: Int
        ) {
            context.startActivity(makeIntent(context, id))
        }
    }
}
