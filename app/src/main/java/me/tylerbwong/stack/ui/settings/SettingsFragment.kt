package me.tylerbwong.stack.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import me.tylerbwong.stack.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        /*Preference(context).apply {
            key = "simple_preference"
            title = "version"
            summary = "some usual summary"
        }*/

    }

}
