package me.tylerbwong.stack.ui.settings.sites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.compose.runtime.Recomposer
import androidx.compose.ui.platform.setContent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.ActivitySettingsBinding
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class SitesActivity : BaseActivity<ActivitySettingsBinding>(
    ActivitySettingsBinding::inflate
), SearchView.OnQueryTextListener {
    private val viewModel by viewModels<SitesViewModel>()

    private var snackbar: Snackbar? = null
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.sites)
        }

        binding.frameContainer.setContent(Recomposer.current()) {
            SitesLayout(::changeSite)
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

    override fun onResume() {
        super.onResume()
        viewModel.currentQuery?.let { searchView?.setQuery(it, true) }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (menu.findItem(R.id.search) == null) {
            menuInflater.inflate(R.menu.menu_sites, menu)
        }

        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as? SearchView
        searchView?.setOnQueryTextListener(this)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        // No-op
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.fetchSites(newText)
        return true
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
