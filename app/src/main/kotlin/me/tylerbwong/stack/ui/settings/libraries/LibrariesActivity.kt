package me.tylerbwong.stack.ui.settings.libraries

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.ui.BaseActivity

@AndroidEntryPoint
class LibrariesActivity : BaseActivity<ViewBinding>(
    bindingProvider = null // TODO Remove when Hilt supports default constructor values
) {
    private val viewModel by viewModels<LibrariesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibrariesScreen(
                libraries = viewModel.libraries,
                onBackPressed = { onBackPressed() }
            )
        }
        viewModel.fetchLibraries(this)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, LibrariesActivity::class.java))
        }
    }
}
