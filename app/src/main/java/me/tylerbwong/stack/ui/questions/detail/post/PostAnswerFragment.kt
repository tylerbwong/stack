package me.tylerbwong.stack.ui.questions.detail.post

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab
import kotlinx.android.synthetic.main.submit_answer_fragment.*
import me.saket.bettermovementmethod.BetterLinkMovementMethod
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.hideKeyboard
import me.tylerbwong.stack.ui.utils.markdown.setMarkdown
import me.tylerbwong.stack.ui.utils.showKeyboard
import me.tylerbwong.stack.ui.utils.showSnackbar

class PostAnswerFragment : Fragment(R.layout.submit_answer_fragment) {

    private val viewModel by viewModels<PostAnswerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.questionId = arguments?.getInt(QuestionDetailActivity.QUESTION_ID, 0) ?: 0

        viewModel.snackbar.observe(this) {
            if (it != null) {
                rootLayout.showSnackbar(it, duration = Snackbar.LENGTH_LONG)

                val activity = activity as? QuestionDetailActivity
                when (it) {
                    R.string.post_answer_success -> activity?.toggleAnswerMode(isInAnswerMode = false)
                }
            }
        }

        previewText.apply {
            setTextIsSelectable(true)
            movementMethod = BetterLinkMovementMethod.getInstance()
        }

        scrollView.setOnScrollChangeListener(OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (scrollY > oldScrollY) {
                postAnswerButton.shrink()
            } else {
                postAnswerButton.extend()
            }
        })

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabReselected(tab: Tab) {
                onTabChanged(tab.position)
            }

            override fun onTabUnselected(tab: Tab) {
                // no-op
            }

            override fun onTabSelected(tab: Tab) {
                onTabChanged(tab.position)
            }
        })

        tabLayout.selectTab(tabLayout.getTabAt(viewModel.selectedTabPosition))

        postAnswerButton.setOnClickListener {
            if (!markdownEditText.text.isNullOrBlank()) {
                viewModel.postAnswer(markdownEditText.text.toString())
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Fix TextInputLayout error message crash
        if (viewModel.markdownTextWatcher != null) {
            markdownEditText.removeTextChangedListener(viewModel.markdownTextWatcher)
            viewModel.markdownTextWatcher = null
        }

        viewModel.markdownTextWatcher = object : TextWatcher {
            override fun afterTextChanged(text: Editable) {
                togglePostAnswerButtonVisibility(isVisible = !text.isBlank())
            }

            override fun beforeTextChanged(text: CharSequence, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(text: CharSequence, start: Int, before: Int, count: Int) {
                markdownInputLayout.error = if (text.isBlank()) {
                    getString(R.string.answer_error)
                } else {
                    null
                }
            }
        }
        markdownEditText.addTextChangedListener(viewModel.markdownTextWatcher)

        togglePostAnswerButtonVisibility()
    }

    private fun onTabChanged(position: Int) {
        viewModel.selectedTabPosition = position
        when (position) {
            0 -> {
                markdownInputLayout.visibility = View.VISIBLE
                previewText.visibility = View.GONE
                markdownEditText.requestFocus()
                markdownEditText.showKeyboard()
            }
            1 -> {
                markdownInputLayout.visibility = View.GONE
                previewText.apply {
                    visibility = View.VISIBLE
                    hideKeyboard()

                    if (!markdownEditText.text.isNullOrBlank()) {
                        gravity = Gravity.START or Gravity.TOP
                        setMarkdown(markdownEditText.text.toString())
                    } else {
                        gravity = Gravity.CENTER
                        setText(R.string.no_preview)
                    }
                }
            }
        }
    }

    private fun togglePostAnswerButtonVisibility(
            isVisible: Boolean = !markdownEditText.text.isNullOrBlank()
    ) = if (isVisible) {
        postAnswerButton.show()
        postAnswerButton.extend()
    } else {
        postAnswerButton.hide()
    }

    companion object {
        fun newInstance(id: Int): PostAnswerFragment {
            return PostAnswerFragment().apply {
                arguments = Bundle().apply {
                    putInt(QuestionDetailActivity.QUESTION_ID, id)
                }
            }
        }
    }
}
