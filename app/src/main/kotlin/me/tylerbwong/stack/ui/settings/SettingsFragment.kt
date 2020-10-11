package me.tylerbwong.stack.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.TwoStatePreference
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.chuckerteam.chucker.api.Chucker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.settings.libraries.LibrariesActivity
import me.tylerbwong.stack.ui.settings.sites.SitesActivity
import me.tylerbwong.stack.ui.theme.ThemeManager.delegateMode
import me.tylerbwong.stack.ui.theme.nightModeOptions
import me.tylerbwong.stack.ui.theme.showThemeChooserDialog
import me.tylerbwong.stack.ui.utils.launchCustomTab
import me.tylerbwong.stack.ui.utils.showSnackbar
import me.tylerbwong.stack.ui.utils.toHtml
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    lateinit var experimental: Experimental

    private val viewModel by viewModels<SettingsViewModel>()

    private val authPreferences = mutableSetOf<Preference>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        if (savedInstanceState == null) {
            addPreferencesFromResource(R.xml.settings)
        }
        with(preferenceManager) {
            findPreference<TwoStatePreference>(getString(R.string.syntax_highlighting))?.apply {
                isVisible = BuildConfig.DEBUG
                isChecked = experimental.syntaxHighlightingEnabled
                setOnPreferenceChangeListener { _, newValue ->
                    experimental.syntaxHighlightingEnabled = newValue as Boolean
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

            findPreference<TwoStatePreference>(getString(R.string.create_question))?.apply {
                isChecked = experimental.createQuestionEnabled
                isVisible = false
                setOnPreferenceChangeListener { _, newValue ->
                    experimental.createQuestionEnabled = newValue as Boolean
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
                authPreferences.add(this)
            }

            setupAppSection()

            findPreference<PreferenceCategory>(getString(R.string.debug))?.isVisible = BuildConfig.DEBUG
            if (BuildConfig.DEBUG) {
                findPreference<Preference>(getString(R.string.inspect_network_traffic))?.apply {
                    setOnPreferenceClickListener {
                        val context = requireContext()
                        val intent = Chucker.getLaunchIntent(context)
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
        viewModel.user.observe(viewLifecycleOwner) { user ->
            findPreference<Preference>(getString(R.string.account))?.apply {
                if (user != null) {
                    title = user.displayName
                    summary = user.location
                    val request = ImageRequest.Builder(requireContext())
                        .data(user.profileImage)
                        .transformations(CircleCropTransformation())
                        .size(resources.getDimensionPixelSize(R.dimen.user_image_placeholder_size))
                        .target { icon = it }
                        .build()
                    imageLoader.enqueue(request)
                    setOnPreferenceClickListener {
                        showLogOutDialog()
                        true
                    }
                } else {
                    title = getString(R.string.log_in)
                    summary = null
                    setIcon(R.drawable.ic_account_circle)
                    setOnPreferenceClickListener {
                        showLogInDialog()
                        true
                    }
                }
                authPreferences.forEach { it.isVisible = BuildConfig.DEBUG && user != null }
            }
        }
        viewModel.currentSite.observe(viewLifecycleOwner) { site ->
            findPreference<Preference>(getString(R.string.current_site))?.apply {
                title = site.name.toHtml()
                summary = site.audience.capitalize().toHtml()
                setOnPreferenceClickListener {
                    SitesActivity.startActivity(requireContext())
                    true
                }
                val request = ImageRequest.Builder(requireContext())
                    .data(site.iconUrl)
                    .target { icon = it }
                    .build()
                imageLoader.enqueue(request)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }

    private fun PreferenceManager.setupAppSection() {
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

        findPreference<Preference>(getString(R.string.source))?.apply {
            setOnPreferenceClickListener {
                launchCustomTab(requireContext(), getString(R.string.repository_url))
                true
            }
        }

        findPreference<Preference>(getString(R.string.libraries))?.apply {
            setOnPreferenceClickListener {
                LibrariesActivity.startActivity(requireContext())
                true
            }
        }

        findPreference<Preference>(getString(R.string.version))?.apply {
            summary = BuildConfig.VERSION_NAME
        }
    }

    private fun showLogOutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.default_dialog_bg))
            .setTitle(R.string.log_out_title)
            .setMessage(R.string.log_out_message)
            .setPositiveButton(R.string.log_out) { _, _ -> viewModel.logOut() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun showLogInDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.default_dialog_bg))
            .setTitle(R.string.log_in_title)
            .setPositiveButton(R.string.log_in) { _, _ ->
                launchCustomTab(requireContext(), AuthStore.authUrl)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }
}
