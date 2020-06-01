package me.tylerbwong.stack.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.DeepLinkResult
import me.tylerbwong.stack.data.DeepLinker
import me.tylerbwong.stack.ui.utils.toHtml
import javax.inject.Inject

class DeepLinkingActivity : BaseActivity<ViewBinding>() {

    @Inject
    lateinit var deepLinker: DeepLinker

    @Inject
    lateinit var deepLinkingViewModelFactory: DeepLinkingViewModelFactory

    private val viewModel by viewModels<DeepLinkingViewModel> { deepLinkingViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.stackComponent.inject(this)
        viewModel.site.observe(this) { site ->
            MaterialAlertDialogBuilder(this)
                .setBackground(ContextCompat.getDrawable(this, R.drawable.default_dialog_bg))
                .setTitle(R.string.change_site_title)
                .setMessage(getString(R.string.not_current_site, site.name.toHtml()))
                .setPositiveButton(R.string.change) { _, _ ->
                    viewModel.changeSite(site.parameter)
                    handleIntent(intent)
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.cancel()
                    finish()
                }
                .setOnDismissListener { finish() }
                .create()
                .show()
        }
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        intent.data?.let {
            when (val result = deepLinker.resolvePath(this, it)) {
                is DeepLinkResult.Success -> {
                    startActivity(result.intent)
                    finish()
                }
                is DeepLinkResult.SiteMismatchError -> viewModel.getSite(result.site)
                is DeepLinkResult.PathNotSupportedError -> {
                    Toast.makeText(this, R.string.deep_link_path_not_supported, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}
