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
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.stack.BuildConfig
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.MainActivity
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.settings.libraries.LibrariesActivity
import me.tylerbwong.stack.ui.settings.sites.SitesActivity
import me.tylerbwong.stack.ui.theme.ThemeManager.delegateMode
import me.tylerbwong.stack.ui.theme.nightModeOptions
import me.tylerbwong.stack.ui.theme.showThemeChooserDialog
import me.tylerbwong.stack.ui.utils.launchUrl
import me.tylerbwong.stack.ui.utils.showLogInDialog
import me.tylerbwong.stack.ui.utils.showLogOutDialog
import me.tylerbwong.stack.ui.utils.showRegisterOnSiteDialog
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
            findPreference<Preference>(getString(R.string.log_out))?.isVisible = false

            if (BuildConfig.DEBUG) {
                findPreference<TwoStatePreference>(getString(R.string.syntax_highlighting))?.apply {
                    isChecked = experimental.syntaxHighlightingEnabled
                    isVisible = BuildConfig.DEBUG
                    setOnPreferenceChangeListener { _, newValue ->
                        experimental.syntaxHighlightingEnabled = newValue as Boolean
                        view?.showRestartSnackbar()
                        true
                    }
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
    @Suppress("LongMethod")
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
                        ProfileActivity.startActivity(context, userId = user.userId)
                        true
                    }
                    findPreference<Preference>(getString(R.string.log_out))?.apply {
                        isVisible = true
                        setOnPreferenceClickListener {
                            requireContext().showLogOutDialog { viewModel.logOut() }
                            true
                        }
                    }
                } else if (viewModel.isAuthenticated.value == true) {
                    title = getString(R.string.register)
                    summary = null
                    setIcon(R.drawable.ic_account_circle)
                    setOnPreferenceClickListener {
                        viewModel.currentSite.value?.let {
                            requireContext().showRegisterOnSiteDialog(
                                site = it,
                                siteUrl = viewModel.buildSiteJoinUrl(it),
                            )
                        }
                        true
                    }
                    findPreference<Preference>(getString(R.string.log_out))?.isVisible = false
                } else {
                    title = getString(R.string.log_in)
                    summary = null
                    setIcon(R.drawable.ic_account_circle)
                    setOnPreferenceClickListener {
                        requireContext().showLogInDialog()
                        true
                    }
                    findPreference<Preference>(getString(R.string.log_out))?.isVisible = false
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
        listView.apply {
            applyInsetter {
                type(ime = true, statusBars = true, navigationBars = true) {
                    padding(bottom = true)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
        setDivider(null)
        setDividerHeight(0)
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

    private fun View.showRestartSnackbar() {
        showSnackbar(
            messageId = R.string.restart_toggle,
            actionTextId = R.string.restart,
            duration = Snackbar.LENGTH_INDEFINITE
        ) {
            val intent = Intent(it.context, MainActivity::class.java)
            ProcessPhoenix.triggerRebirth(it.context, intent)
        }
    }
}
