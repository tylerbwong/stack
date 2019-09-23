package me.tylerbwong.stack.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.BaseActivity

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(settings_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.settings)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameContainer, SettingsFragment())
            .commit()
    }

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, SettingsActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
