package me.tylerbwong.stack.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.reviewer.AppReviewer
import me.tylerbwong.stack.data.updater.AppUpdater
import me.tylerbwong.stack.data.work.WorkScheduler
import me.tylerbwong.stack.databinding.ActivityMainBinding
import me.tylerbwong.stack.ui.drafts.DraftsFragment
import me.tylerbwong.stack.ui.home.HomeFragment
import me.tylerbwong.stack.ui.inbox.InboxFragment
import me.tylerbwong.stack.ui.questions.ask.AskQuestionActivity
import me.tylerbwong.stack.ui.search.SearchFragment
import me.tylerbwong.stack.ui.settings.SettingsActivity
import me.tylerbwong.stack.ui.settings.sites.SitesActivity
import me.tylerbwong.stack.ui.shortcuts.pushAskQuestionShortcut
import me.tylerbwong.stack.ui.shortcuts.removeAskQuestionShortcut
import me.tylerbwong.stack.ui.utils.hideKeyboard
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.showSnackbar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var workScheduler: WorkScheduler

    @Inject
    lateinit var appUpdater: AppUpdater

    @Inject
    lateinit var appReviewer: AppReviewer

    private val viewModel by viewModels<MainViewModel>()

    private val homeFragment by lazy { initializeFragment(HOME_FRAGMENT_TAG) { HomeFragment() } }
    private val searchFragment by lazy { initializeFragment(SEARCH_FRAGMENT_TAG) { SearchFragment() } }
    private val inboxFragment by lazy { initializeFragment(INBOX_FRAGMENT_TAG) { InboxFragment() } }
    private val draftsFragment by lazy { initializeFragment(DRAFTS_FRAGMENT_TAG) { DraftsFragment() } }

    private val authTabIds = listOf(R.id.ask, R.id.inbox, R.id.drafts)

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult(),
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            checkForPendingInstall()
        } else {
            showUpdateNotDownloaded()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        setupBottomNavigation()

        supportActionBar?.title = ""

        viewModel.isAuthenticatedLiveData.observe(this) { isAuthenticated ->
            val bottomNav = binding.bottomNav
            authTabIds.forEach {
                bottomNav.menu.findItem(it)?.isVisible = isAuthenticated
            }
            if (bottomNav.selectedItemId in authTabIds) {
                bottomNav.selectedItemId = R.id.home
            }
            if (isAuthenticated) {
                pushAskQuestionShortcut()
                // TODO Figure out unread status
                // viewModel.fetchInboxUnread()
            } else {
                removeAskQuestionShortcut()
            }
        }
        viewModel.siteLiveData.observe(this) { viewModel.fetchSites() }
        viewModel.currentSite.observe(this) { site ->
            if (site != null) {
                binding.siteIcon.apply {
                    load(site.iconUrl)
                    setThrottledOnClickListener {
                        // TODO Switch to bottom sheet?
                        SitesActivity.startActivity(this@MainActivity)
                    }
                }
            }
        }
        // TODO Figure out unread status
//        viewModel.inboxUnreadCount.observe(this) { unreadCount ->
//            if (unreadCount != null) {
//                val inboxBadge = binding.bottomNav.getOrCreateBadge(R.id.inbox)
//                if (unreadCount > 0) {
//                    inboxBadge.isVisible = true
//                    inboxBadge.number = unreadCount
//                } else {
//                    inboxBadge.isVisible = false
//                    inboxBadge.number = 0
//                }
//            }
//        }

        appUpdater.checkForUpdate(::checkForPendingInstall, activityResultLauncher)
        workScheduler.schedule()
        populateContent(savedInstanceState)
        val selectedTab = intent.getIntExtra(SELECTED_TAB, 0)
        if (selectedTab != 0) {
            binding.bottomNav.selectedItemId = binding.bottomNav.menu.getItem(selectedTab).itemId
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchSites()
        checkForPendingInstall()
        appReviewer.initializeReviewFlow(activity = this)
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

    override fun onDestroy() {
        super.onDestroy()
        appUpdater.unregisterListener()
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
            setOnItemSelectedListener { menuItem ->
                if (menuItem.itemId == R.id.ask) {
                    AskQuestionActivity.startActivity(this@MainActivity)
                    return@setOnItemSelectedListener false
                }

                val (fragment, liftOnScrollId) = when (menuItem.itemId) {
                    R.id.search -> searchFragment to R.id.searchRecycler
                    R.id.inbox -> inboxFragment to R.id.inboxRecycler
                    R.id.drafts -> draftsFragment to R.id.draftsRecycler
                    else -> homeFragment to R.id.homeRecycler
                }
                val currentFragment = supportFragmentManager.fragments
                    .firstOrNull { !it.isHidden }
                    ?: homeFragment

                supportFragmentManager
                    .beginTransaction()
                    .hide(currentFragment)
                    .show(fragment)
                    .commit()

                binding.appBar.liftOnScrollTargetViewId = liftOnScrollId

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
            binding.appBar.liftOnScrollTargetViewId = R.id.homeRecycler
        }
    }

    private fun showUpdateNotDownloaded() {
        binding.bottomNav.showSnackbar(
            R.string.update_not_downloaded,
            R.string.update,
            Snackbar.LENGTH_LONG,
            true
        ) { appUpdater.checkForUpdate(::checkForPendingInstall, activityResultLauncher) }
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
                        unregisterListener()
                    }
                }
            },
            onDownloadFailed = {
                binding.bottomNav.showSnackbar(
                    R.string.download_error,
                    R.string.retry,
                    shouldAnchorView = true
                ) {
                    appUpdater.checkForUpdate(::checkForPendingInstall, activityResultLauncher)
                }
            }
        )
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
        private const val INBOX_FRAGMENT_TAG = "inbox_fragment"
        private const val DRAFTS_FRAGMENT_TAG = "drafts_fragment"
        private const val SELECTED_TAB = "selected_tab"

        fun makeIntentClearTop(
            context: Context,
            selectedTab: Int = 0,
        ) = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            .putExtra(SELECTED_TAB, selectedTab)
    }
}
