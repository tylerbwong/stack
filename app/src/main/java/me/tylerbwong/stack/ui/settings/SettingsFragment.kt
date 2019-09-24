package me.tylerbwong.stack.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.theme.ThemeManager.delegateMode
import me.tylerbwong.stack.ui.theme.nightModeOptions
import me.tylerbwong.stack.ui.theme.showThemeChooserDialog

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        with(preferenceManager) {
            findPreference<Preference>(getString(R.string.theme))?.apply {
                summary = getString(
                    nightModeOptions
                        .filterValues { it == context.delegateMode }
                        .keys
                        .firstOrNull() ?: R.string.theme_light
                )
                setOnPreferenceClickListener {
                    context.showThemeChooserDialog()
                    true
                }
            }

            findPreference<Preference>(getString(R.string.version))?.apply {
                summary = BuildConfig.VERSION_NAME
            }
        }
    }
}
