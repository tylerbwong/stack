package me.tylerbwong.stack.ui.flag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.BaseActivity

@AndroidEntryPoint
class FlagActivity : BaseActivity<ViewBinding>(
    bindingProvider = null // TODO Remove when Hilt supports default constructor values
) {
    private val viewModel by viewModels<FlagViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val postId = intent.getIntExtra(POST_ID, -1)
        val postType = intent.getIntExtra(POST_TYPE, -1)

        if (postId != -1 && postType == -1) {
            finish()
            return
        }

        viewModel.postType = when (postType) {
            0 -> FlagViewModel.FlagPostType.Question(id = postId)
            1 -> FlagViewModel.FlagPostType.Answer(id = postId)
            2 -> FlagViewModel.FlagPostType.Comment(id = postId)
            else -> {
                finish()
                null
            }
        }
        viewModel.success.observe(this) { success ->
            if (success == true) {
                Toast.makeText(this, R.string.flag_success, Toast.LENGTH_LONG).show()
                finish()
            } else if (success == false) {
                Toast.makeText(this, R.string.flag_fail, Toast.LENGTH_LONG).show()
            }
        }
        viewModel.getFlagOptions()

        setContent {
            FlagScreen(onBackPressed = onBackPressedDispatcher::onBackPressed)
        }
    }

    override fun applyFullscreenWindowInsets() {
        // No-op, handled manually for Compose
    }

    companion object {
        private const val POST_ID = "post_id"
        private const val POST_TYPE = "post_type"

        fun makeIntent(
            context: Context,
            postId: Int,
            postType: Int,
        ): Intent = Intent(context, FlagActivity::class.java)
            .putExtra(POST_ID, postId)
            .putExtra(POST_TYPE, postType)
    }
}
