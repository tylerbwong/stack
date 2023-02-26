package me.tylerbwong.stack.ui.questions.ask

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.ActivityAskQuestionBinding
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.questions.detail.QuestionDetailActivity
import me.tylerbwong.stack.ui.utils.formatElapsedTime

@AndroidEntryPoint
class AskQuestionActivity : BaseActivity<ActivityAskQuestionBinding>(
    ActivityAskQuestionBinding::inflate
) {
    private val viewModel by viewModels<AskQuestionViewModel>()
    private val timestampProvider: (Long) -> String = { it.formatElapsedTime(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.composeContent.setContent {
            AskQuestionLayout(onFinish = ::finish)
        }
        viewModel.askQuestionState.observe(this) { state ->
            when (state) {
                is AskQuestionSuccessPreview -> showSnackbar(getString(R.string.preview_success))
                is AskQuestionSuccess -> {
                    QuestionDetailActivity.startActivity(this, state.questionId)
                    finish()
                }
                is AskQuestionError -> showSnackbar(state.errorMessage)
            }
        }
        viewModel.fetchDraft( -1, timestampProvider)
    }

    override fun applyFullscreenWindowInsets() {
        // No-op, handled manually for Compose
    }

    private fun showSnackbar(message: String?) {
        with(
            Snackbar.make(
                binding.root,
                message ?: getString(R.string.network_error),
                Snackbar.LENGTH_INDEFINITE
            )
        ) {
            this@with.view.findViewById<TextView>(
                com.google.android.material.R.id.snackbar_text
            )?.apply {
                maxLines = 4
            }
            setAction(R.string.dismiss) { dismiss() }
            show()
        }
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, AskQuestionActivity::class.java)
            context.startActivity(intent)
        }
    }
}
