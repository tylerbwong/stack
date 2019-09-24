package me.tylerbwong.stack.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val screen = preferenceManager.createPreferenceScreen(context)

        val versionPref = Preference(context).apply {
            key = "version_key"
            summary = BuildConfig.VERSION_NAME
            title = getString(R.string.version)
        }

        screen.addPreference(versionPref)

        preferenceScreen = screen
    }
}
