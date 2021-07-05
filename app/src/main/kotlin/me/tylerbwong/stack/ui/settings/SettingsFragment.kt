package me.tylerbwong.stack.ui.settings

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.TwoStatePreference
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.chuckerteam.chucker.api.Chucker
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
import me.tylerbwong.stack.ui.utils.launchUrl
import me.tylerbwong.stack.ui.utils.showDialog
import me.tylerbwong.stack.ui.utils.showSnackbar
import me.tylerbwong.stack.ui.utils.toHtml
import java.util.Locale
import javax.inject.Inject
import me.tylerbwong.stack.api.BuildConfig as ApiBuildConfig

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
            listOfNotNull(
                findPreference(getString(R.string.experimental)),
                findPreference(getString(R.string.debug))
            ).forEach { it.isVisible = BuildConfig.DEBUG }

            if (BuildConfig.DEBUG) {
                findPreference<TwoStatePreference>(getString(R.string.syntax_highlighting))?.apply {
                    isChecked = experimental.syntaxHighlightingEnabled
                    isVisible = BuildConfig.DEBUG
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

                findPreference<Preference>(getString(R.string.inspect_network_traffic))?.apply {
                    setOnPreferenceClickListener {
                        val context = requireContext()
                        val intent = Chucker.getLaunchIntent(context)
                        context.startActivity(intent)
                        true
                    }
                }
            }

            setUpAppSection()
            setUpAboutSection()
        }
        viewModel.isAuthenticated.observe(this) { isAuthenticated ->
            authPreferences.forEach { it.isVisible = isAuthenticated && BuildConfig.DEBUG }
        }
    }

    @SuppressLint("DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView?.isVerticalScrollBarEnabled = false
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
            }
        }
        viewModel.currentSite.observe(viewLifecycleOwner) { site ->
            findPreference<Preference>(getString(R.string.current_site))?.apply {
                title = site.name.toHtml()
                summary = site.audience
                    .replaceFirstChar {
                        if (it.isLowerCase()) {
                            it.titlecase(Locale.getDefault())
                        } else {
                            it.toString()
                        }
                    }
                    .toHtml()
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

    private fun PreferenceManager.setUpAppSection() {
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
            val versionCode = requireContext().resources.getInteger(R.integer.version_code)
            summary = "${BuildConfig.VERSION_NAME} ${getString(R.string.item_count, versionCode)}"
        }
    }

    private fun PreferenceManager.setUpAboutSection() {
        findPreference<Preference>(getString(R.string.source))?.apply {
            setOnPreferenceClickListener {
                requireContext().launchUrl(getString(R.string.repository_url))
                true
            }
        }

        findPreference<Preference>(getString(R.string.libraries))?.apply {
            setOnPreferenceClickListener {
                LibrariesActivity.startActivity(requireContext())
                true
            }
        }

        findPreference<Preference>(getString(R.string.api))?.apply {
            summary = "v${ApiBuildConfig.API_VERSION}"
            setOnPreferenceClickListener {
                requireContext().launchUrl(getString(R.string.api_home_url))
                true
            }
        }

        findPreference<Preference>(getString(R.string.privacy))?.apply {
            setOnPreferenceClickListener {
                requireContext().launchUrl(getString(R.string.privacy_url))
                true
            }
        }

        findPreference<Preference>(getString(R.string.terms))?.apply {
            setOnPreferenceClickListener {
                requireContext().launchUrl(getString(R.string.terms_url))
                true
            }
        }
    }

    private fun showLogOutDialog() {
        requireContext().showDialog {
            setTitle(R.string.log_out_title)
            setMessage(R.string.log_out_message)
            setPositiveButton(R.string.log_out) { _, _ -> viewModel.logOut() }
            setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
        }
    }

    private fun showLogInDialog() {
        requireContext().showDialog {
            setTitle(R.string.log_in_title)
            setPositiveButton(R.string.log_in) { _, _ ->
                requireContext().launchUrl(AuthStore.authUrl)
            }
            setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
        }
    }
}
