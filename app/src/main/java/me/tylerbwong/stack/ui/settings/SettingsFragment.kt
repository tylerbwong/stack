package me.tylerbwong.stack.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.TwoStatePreference
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.processphoenix.ProcessPhoenix
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.theme.ThemeManager.delegateMode
import me.tylerbwong.stack.ui.theme.nightModeOptions
import me.tylerbwong.stack.ui.theme.showThemeChooserDialog
import me.tylerbwong.stack.ui.utils.markdown.Markdown
import me.tylerbwong.stack.ui.utils.showSnackbar

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        if (savedInstanceState == null) {
            addPreferencesFromResource(R.xml.settings)
        }
        with(preferenceManager) {
            findPreference<TwoStatePreference>(getString(R.string.syntax_highlighting))?.apply {
                isChecked = Markdown.experimentalSyntaxHighlightingEnabled
                setOnPreferenceChangeListener { _, newValue ->
                    Markdown.experimentalSyntaxHighlightingEnabled = newValue as Boolean
                    view?.showSnackbar(
                        messageId = R.string.restart_toggle,
                        actionTextId = R.string.restart,
                        duration = Snackbar.LENGTH_INDEFINITE
                    ) {
                        val intent = Intent(it.context, MainActivity::class.java)
                        ProcessPhoenix.triggerRebirth(it.context, intent)
                    }
                    true
                }
            }

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
