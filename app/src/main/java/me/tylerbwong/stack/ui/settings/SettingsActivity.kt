package me.tylerbwong.stack.ui.settings

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
        supportActionBar?.setTitle(R.string.settings)

        text_app_version.text = resources.getString(R.string.version_name)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
