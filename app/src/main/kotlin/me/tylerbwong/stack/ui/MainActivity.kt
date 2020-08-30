package me.tylerbwong.stack.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import coil.api.load
import coil.transform.CircleCropTransformation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.AppUpdater
import me.tylerbwong.stack.data.auth.AuthStore
import me.tylerbwong.stack.data.work.WorkScheduler
import me.tylerbwong.stack.databinding.ActivityMainBinding
import me.tylerbwong.stack.ui.bookmarks.BookmarksFragment
import me.tylerbwong.stack.ui.drafts.DraftsFragment
import me.tylerbwong.stack.ui.home.HomeFragment
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.questions.create.CreateQuestionActivity
import me.tylerbwong.stack.ui.search.SearchFragment
import me.tylerbwong.stack.ui.settings.Experimental
import me.tylerbwong.stack.ui.settings.SettingsActivity
import me.tylerbwong.stack.ui.utils.hideKeyboard
import me.tylerbwong.stack.ui.utils.launchCustomTab
import me.tylerbwong.stack.ui.utils.ofType
import me.tylerbwong.stack.ui.utils.setSharedTransition
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.showSnackbar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    ActivityMainBinding::inflate
), InstallStateUpdatedListener {

    @Inject
    lateinit var workScheduler: WorkScheduler

    @Inject
    lateinit var experimental: Experimental

    private val viewModel by viewModels<MainViewModel>()

    private lateinit var appUpdater: AppUpdater

    private val homeFragment by lazy { initializeFragment(HOME_FRAGMENT_TAG) { HomeFragment() } }
    private val searchFragment by lazy { initializeFragment(SEARCH_FRAGMENT_TAG) { SearchFragment() } }
    private val bookmarksFragment by lazy { initializeFragment(BOOKMARKS_FRAGMENT_TAG) { BookmarksFragment() } }
    private val draftsFragment by lazy { initializeFragment(DRAFTS_FRAGMENT_TAG) { DraftsFragment() } }

    private val authTabIds = listOf(R.id.create, R.id.bookmarks, R.id.drafts)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        setupBottomNavigation()

        setSharedTransition(
            android.R.id.statusBarBackground,
            android.R.id.navigationBarBackground
        )

        supportActionBar?.title = ""

        viewModel.isAuthenticated.observe(this) { isAuthenticated ->
            val bottomNav = binding.bottomNav
            authTabIds.forEach { bottomNav.menu.findItem(it)?.isVisible = isAuthenticated }
            if (bottomNav.selectedItemId in authTabIds) {
                bottomNav.selectedItemId = R.id.home
            }

            if (isAuthenticated) {
                viewModel.fetchUser()
            } else {
                with(binding.profileIcon) {
                    setImageResource(R.drawable.ic_account_circle)
                    setThrottledOnClickListener { showLogInDialog() }
                }
            }
        }
        viewModel.user.observe(this) { user ->
            binding.profileIcon.apply {
                if (user != null) {
                    load(user.profileImage) {
                        error(R.drawable.user_image_placeholder)
                        placeholder(R.drawable.user_image_placeholder)
                        transformations(CircleCropTransformation())
                    }
                    setThrottledOnClickListener {
                        val aoc = context.ofType<Activity>()?.let {
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                it,
                                Pair(this, context.getString(R.string.shared_transition_name))
                            )
                        }
                        ProfileActivity.startActivity(
                            this@MainActivity,
                            userId = user.userId,
                            extras = aoc?.toBundle()
                        )
                    }
                } else {
                    setImageResource(R.drawable.ic_account_circle)
                    setThrottledOnClickListener { showLogInDialog() }
                }
            }
        }

        appUpdater = AppUpdater(AppUpdateManagerFactory.create(this))
        appUpdater.checkForUpdate(this)
        workScheduler.schedule()
        populateContent(savedInstanceState)
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

    @Suppress("deprecation") // Until play core supports new activity result API
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUpdater.APP_UPDATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                checkForPendingInstall()
            } else {
                binding.bottomNav.showSnackbar(
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
        Insetter.builder().setOnApplyInsetsListener { view, insets, initialState ->
            view.updatePadding(
                bottom = initialState.paddings.bottom + insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
                ).bottom
            )
        }.applyToView(binding.bottomNav)
    }

    private fun setupBottomNavigation() {
        with(binding.bottomNav) {
            post { menu.findItem(R.id.create)?.isVisible = experimental.createQuestionEnabled }
            setOnNavigationItemSelectedListener { menuItem ->
                if (experimental.createQuestionEnabled && menuItem.itemId == R.id.create) {
                    CreateQuestionActivity.startActivity(this@MainActivity)
                    return@setOnNavigationItemSelectedListener false
                }

                val fragment = when (menuItem.itemId) {
                    R.id.search -> searchFragment
                    R.id.bookmarks -> bookmarksFragment
                    R.id.drafts -> draftsFragment
                    else -> homeFragment
                }
                val currentFragment = supportFragmentManager.fragments
                    .firstOrNull { !it.isHidden }
                    ?: homeFragment

                supportFragmentManager
                    .beginTransaction()
                    .hide(currentFragment)
                    .show(fragment)
                    .commit()

                invalidateOptionsMenu()

                hideKeyboard()

                true
            }
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
                binding.bottomNav.showSnackbar(
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
                binding.bottomNav.showSnackbar(
                    R.string.download_error,
                    R.string.retry,
                    shouldAnchorView = true
                ) {
                    appUpdater.checkForUpdate(this)
                }
            }
        )
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
        private const val BOOKMARKS_FRAGMENT_TAG = "bookmarks_fragment"
        private const val DRAFTS_FRAGMENT_TAG = "drafts_fragment"

        fun makeIntentClearTop(context: Context) = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
}
