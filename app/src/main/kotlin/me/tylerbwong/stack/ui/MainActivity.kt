package me.tylerbwong.stack.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.setupWithNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.reviewer.AppReviewer
import me.tylerbwong.stack.data.updater.AppUpdater
import me.tylerbwong.stack.data.work.WorkScheduler
import me.tylerbwong.stack.databinding.ActivityMainBinding
import me.tylerbwong.stack.ui.profile.ProfileActivity
import me.tylerbwong.stack.ui.settings.Experimental
import me.tylerbwong.stack.ui.settings.SettingsActivity
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.showLogInDialog
import me.tylerbwong.stack.ui.utils.showRegisterOnSiteDialog
import me.tylerbwong.stack.ui.utils.showSnackbar
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    @Inject
    lateinit var workScheduler: WorkScheduler

    @Inject
    lateinit var experimental: Experimental

    @Inject
    lateinit var appUpdater: AppUpdater

    @Inject
    lateinit var appReviewer: AppReviewer

    private val viewModel by viewModels<MainViewModel>()

    private val authTabIds = listOf(R.id.create, R.id.bookmarks, R.id.drafts)
    private val navController: NavController
        get() = (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment)
            .navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)
        binding.bottomNav.setupWithNavController(navController)

        supportActionBar?.title = ""

        viewModel.isAuthenticatedLiveData.observe(this) { isAuthenticated ->
            val bottomNav = binding.bottomNav
            val isCreateQuestionEnabled = experimental.createQuestionEnabled
            authTabIds.forEach {
                bottomNav.menu.findItem(it)?.isVisible = if (it == R.id.create) {
                    isAuthenticated && isCreateQuestionEnabled
                } else {
                    isAuthenticated
                }
            }
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
                        ProfileActivity.startActivity(this@MainActivity, userId = user.userId)
                    }
                } else if (viewModel.isAuthenticatedLiveData.value == true) {
                    setImageResource(R.drawable.ic_account_circle)
                    setThrottledOnClickListener {
                        viewModel.currentSite.value?.let {
                            showRegisterOnSiteDialog(
                                site = it,
                                siteUrl = viewModel.buildSiteJoinUrl(it),
                            )
                        }
                    }
                } else {
                    setImageResource(R.drawable.ic_account_circle)
                    setThrottledOnClickListener { showLogInDialog() }
                }
            }
        }

        appUpdater.checkForUpdate(this)
        workScheduler.schedule()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUser()
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

    internal fun checkForPendingInstall() {
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
                    appUpdater.checkForUpdate(this)
                }
            }
        )
    }

    private fun navigateTo(@IdRes navigationId: Int) {
        navController.navigate(
            resId = navigationId,
            args = null,
            navOptions = navOptions {
                launchSingleTop = true
                restoreState = true
            }
        )
    }

    companion object {
        fun makeIntentClearTop(context: Context) = Intent(context, MainActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
}
