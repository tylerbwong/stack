package me.tylerbwong.stack.ui.settings.libraries

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.ui.BaseActivity

@AndroidEntryPoint
class LibrariesActivity : BaseActivity<ViewBinding>(
    bindingProvider = null // TODO Remove when Hilt supports default constructor values
) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            LibrariesScreen(onBackPressedDispatcher::onBackPressed)
        }
    }

    override fun applyFullscreenWindowInsets() {
        // No-op, handled manually for Compose
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LibrariesActivity::class.java))
        }
    }
}
