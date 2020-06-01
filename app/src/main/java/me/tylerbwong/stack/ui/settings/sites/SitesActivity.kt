package me.tylerbwong.stack.ui.settings.sites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.ActivitySettingsBinding
import me.tylerbwong.stack.ui.BaseActivity

class SitesActivity : BaseActivity<ActivitySettingsBinding>(ActivitySettingsBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.sites)
        }

        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameContainer, SitesFragment())
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, SitesActivity::class.java)
            context.startActivity(intent)
        }
    }
}
