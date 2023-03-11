package me.tylerbwong.stack.ui.settings.donation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.ui.BaseActivity

@AndroidEntryPoint
class DonationActivity : BaseActivity<ViewBinding>(
    bindingProvider = null // TODO Remove when Hilt supports default constructor values
) {
    private val viewModel by viewModels<DonationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DonationScreen(viewModel) { this.onBackPressed() }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startConnection()
    }

    override fun applyFullscreenWindowInsets() {
        // No-op, handled manually for Compose
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, DonationActivity::class.java))
        }
    }
}
