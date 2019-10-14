package me.tylerbwong.stack.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import coil.api.load
import coil.transform.CircleCropTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import kotlinx.android.synthetic.main.activity_main.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.AppUpdater
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.ui.drafts.DraftsFragment
import me.tylerbwong.stack.ui.home.HomeFragment
import me.tylerbwong.stack.ui.search.SearchFragment
import me.tylerbwong.stack.ui.settings.SettingsActivity
import me.tylerbwong.stack.ui.utils.hideKeyboard
import me.tylerbwong.stack.ui.utils.launchCustomTab
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.showSnackbar
import javax.inject.Inject

class MainActivity : BaseActivity(R.layout.activity_main), InstallStateUpdatedListener {

    @Inject
    lateinit var mainViewModelFactory: MainViewModelFactory

    private val viewModel by viewModels<MainViewModel> { mainViewModelFactory }

    private lateinit var appUpdater: AppUpdater

    private val homeFragment by lazy { initializeFragment(HOME_FRAGMENT_TAG) { HomeFragment() } }
    private val searchFragment by lazy { initializeFragment(SEARCH_FRAGMENT_TAG) { SearchFragment() } }
    private val draftsFragment by lazy { initializeFragment(DRAFTS_FRAGMENT_TAG) { DraftsFragment() } }

    private val authTabIds = listOf(R.id.drafts)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.uiComponent.inject(this)
        setSupportActionBar(toolbar)
        setupBottomNavigation()
        setupFade()

        supportActionBar?.title = ""

        viewModel.isAuthenticated.observe(this) { isAuthenticated ->
            authTabIds.forEach { bottomNav.menu.findItem(it)?.isVisible = isAuthenticated }
            if (bottomNav.selectedItemId in authTabIds) {
                bottomNav.selectedItemId = R.id.home
            }

            if (isAuthenticated) {
                viewModel.fetchUser()
                profileIcon.setThrottledOnClickListener { showLogOutDialog() }
            } else {
                profileIcon.setThrottledOnClickListener { showLogInDialog() }
            }
        }
        viewModel.profileImage.observe(this) {
            profileIcon.apply {
                if (it != null) {
                    load(it) {
                        crossfade(true)
                        error(R.drawable.user_image_placeholder)
                        placeholder(R.drawable.user_image_placeholder)
                        transformations(CircleCropTransformation())
                    }
                } else {
                    setImageResource(R.drawable.ic_account_circle)
                }
            }
        }

        appUpdater = AppUpdater(AppUpdateManagerFactory.create(this))
        appUpdater.checkForUpdate(this)

        populateContent(savedInstanceState)
    }

    private fun setupFade() {
        val fade = Fade()
        fade.excludeTarget(R.id.appBar, true)
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        window.enterTransition = fade
        window.exitTransition = fade
    }

    override fun onResume() {
        super.onResume()
        checkForPendingInstall()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                SettingsActivity.startActivity(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUpdater.APP_UPDATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                checkForPendingInstall()
            } else {
                bottomNav.showSnackbar(
                    R.string.update_not_downloaded,
                    R.string.update,
                    Snackbar.LENGTH_LONG,
                    true
                ) { appUpdater.checkForUpdate(this) }
            }
        }
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            checkForPendingInstall()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdater.unregisterListener(this)
    }

    override fun applyFullscreenWindowInsets() {
        super.applyFullscreenWindowInsets()
        bottomNav.doOnApplyWindowInsets { view, insets, initialState ->
            view.updatePadding(
                bottom = initialState.paddings.bottom + insets.systemWindowInsetBottom
            )
        }
    }

    private fun setupBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.search -> searchFragment
                R.id.drafts -> draftsFragment
                else -> homeFragment
            }
            val currentFragment =
                supportFragmentManager.fragments.firstOrNull { !it.isHidden } ?: homeFragment

            supportFragmentManager
                .beginTransaction()
                .hide(currentFragment)
                .show(fragment)
                .commit()

            bottomNav.hideKeyboard()

            true
        }
    }

    private fun populateContent(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .show(homeFragment)
                .commit()
        }
    }

    private fun checkForPendingInstall() {
        appUpdater.checkForPendingInstall(
            onDownloadFinished = {
                bottomNav.showSnackbar(
                    R.string.restart_to_install,
                    R.string.restart,
                    shouldAnchorView = true
                ) {
                    appUpdater.apply {
                        completeUpdate()
                        unregisterListener(this@MainActivity)
                    }
                }
            },
            onDownloadFailed = {
                bottomNav.showSnackbar(
                    R.string.download_error,
                    R.string.retry,
                    shouldAnchorView = true
                ) {
                    appUpdater.checkForUpdate(this)
                }
            }
        )
    }

    private fun showLogOutDialog() {
        MaterialAlertDialogBuilder(this)
            .setBackground(ContextCompat.getDrawable(this, R.drawable.default_dialog_bg))
            .setTitle(R.string.log_out_title)
            .setMessage(R.string.log_out_message)
            .setPositiveButton(R.string.log_out) { _, _ -> viewModel.logOut() }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun showLogInDialog() {
        MaterialAlertDialogBuilder(this)
            .setBackground(ContextCompat.getDrawable(this, R.drawable.default_dialog_bg))
            .setTitle(R.string.log_in_title)
            .setPositiveButton(R.string.log_in) { _, _ ->
                launchCustomTab(this, AuthStore.authUrl)
            }
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            .create()
            .show()
    }

    private fun initializeFragment(tag: String, createFragment: () -> Fragment): Fragment {
        return supportFragmentManager.findFragmentByTag(tag) ?: createFragment().also { fragment ->
            supportFragmentManager
                .beginTransaction()
                .add(R.id.contentContainer, fragment, tag)
                .hide(fragment)
                .commit()
        }
    }

    companion object {
        private const val HOME_FRAGMENT_TAG = "home_fragment"
        private const val SEARCH_FRAGMENT_TAG = "search_fragment"
        private const val DRAFTS_FRAGMENT_TAG = "drafts_fragment"

        fun makeIntentClearTop(context: Context) = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
}