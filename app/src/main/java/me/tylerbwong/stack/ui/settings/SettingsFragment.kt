package me.tylerbwong.stack.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.TwoStatePreference
import coil.ImageLoader
import coil.request.LoadRequest
import com.chuckerteam.chucker.api.Chucker
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.processphoenix.ProcessPhoenix
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.ApplicationWrapper
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.settings.sites.SitesActivity
import me.tylerbwong.stack.ui.theme.ThemeManager.delegateMode
import me.tylerbwong.stack.ui.theme.nightModeOptions
import me.tylerbwong.stack.ui.theme.showThemeChooserDialog
import me.tylerbwong.stack.ui.utils.markdown.Markdown
import me.tylerbwong.stack.ui.utils.showSnackbar
import me.tylerbwong.stack.ui.utils.toHtml
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var settingsViewModelFactory: SettingsViewModelFactory

    private val viewModel by viewModels<SettingsViewModel> { settingsViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        ApplicationWrapper.stackComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

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
                    context.showThemeChooserDialog {
                        summary = getString(
                            nightModeOptions
                                .filterValues { it == context.delegateMode }
                                .keys
                                .firstOrNull() ?: R.string.theme_light
                        )
                    }
                    true
                }
            }

            findPreference<Preference>(getString(R.string.version))?.apply {
                summary = BuildConfig.VERSION_NAME
            }

            findPreference<PreferenceCategory>(getString(R.string.debug))?.isVisible = BuildConfig.DEBUG
            if (BuildConfig.DEBUG) {
                findPreference<Preference>(getString(R.string.inspect_network_traffic))?.apply {
                    setOnPreferenceClickListener {
                        val context = requireContext()
                        val intent = Chucker.getLaunchIntent(context, Chucker.SCREEN_HTTP)
                        context.startActivity(intent)
                        true
                    }
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.currentSite.observe(viewLifecycleOwner) { site ->
            findPreference<Preference>(getString(R.string.current_site))?.apply {
                title = site.name.toHtml()
                summary = site.audience.capitalize().toHtml()
                setOnPreferenceClickListener {
                    SitesActivity.startActivity(requireContext())
                    true
                }
                val request = LoadRequest.Builder(requireContext())
                    .crossfade(true)
                    .data(site.iconUrl)
                    .target { icon = it }
                    .build()
                imageLoader.execute(request)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchCurrentSite()
    }
}
