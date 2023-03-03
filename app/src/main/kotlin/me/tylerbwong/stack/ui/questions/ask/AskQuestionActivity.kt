package me.tylerbwong.stack.ui.questions.ask

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.databinding.ActivityAskQuestionBinding
import me.tylerbwong.stack.ui.BaseActivity

@AndroidEntryPoint
class AskQuestionActivity : BaseActivity<ActivityAskQuestionBinding>(
    ActivityAskQuestionBinding::inflate
) {
    private val viewModel by viewModels<AskQuestionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel.fetchDraft(id = intent.getIntExtra(DRAFT_ID_EXTRA, -1))
        binding.composeContent.setContent {
            AskQuestionLayout(onFinish = ::finish)
        }
    }

    override fun applyFullscreenWindowInsets() {
        // No-op, handled manually for Compose
    }

    companion object {
        private const val DRAFT_ID_EXTRA = "draft_id"

        fun startActivity(context: Context, draftId: Int? = null) {
            val intent = Intent(context, AskQuestionActivity::class.java)
            if (draftId != null) {
                intent.putExtra(DRAFT_ID_EXTRA, draftId)
            }
            context.startActivity(intent)
        }
    }
}
