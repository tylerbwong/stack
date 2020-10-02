package me.tylerbwong.stack.ui.settings.sites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.ui.platform.setContent
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class SitesActivity : BaseActivity<ViewBinding>(
    bindingProvider = null // TODO Remove when Hilt supports default constructor values
) {
    private val viewModel by viewModels<SitesViewModel>()

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SitesScreen(::changeSite, ::onBackPressed)
        }

        viewModel.snackbar.observe(this) {
            if (it != null) {
                window?.decorView?.post {
                    snackbar = window?.decorView?.showSnackbar(
                        R.string.network_error,
                        duration = Snackbar.LENGTH_LONG
                    )
                }
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.logOutState.observe(this) { state ->
            when (state) {
                is SiteLogOutResult.SiteLogOutSuccess -> changeSite(state.siteParameter)
                else -> Unit // No-op
            }
        }

        viewModel.fetchSites()
    }

    private fun changeSite(siteParameter: String) {
        viewModel.changeSite(siteParameter)
        finish()
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, SitesActivity::class.java)
            context.startActivity(intent)
        }
    }
}
