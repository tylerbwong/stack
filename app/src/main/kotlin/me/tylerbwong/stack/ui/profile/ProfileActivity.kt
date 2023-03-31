package me.tylerbwong.stack.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.ActivityProfileBinding
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.showDialog
import me.tylerbwong.stack.ui.utils.showSnackbar
import me.tylerbwong.stack.ui.utils.toHtml

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var adapter: ProfilePagerAdapter
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        viewModel.userId = intent.getIntExtra(USER_ID, 0)
        viewModel.snackbar.observe(this) {
            if (it != null) {
                snackbar = binding.rootLayout.showSnackbar(R.string.network_error, R.string.retry) {
                    viewModel.fetchUserData()
                }
            } else {
                snackbar?.dismiss()
            }
        }

        viewModel.userData.observe(this) {
            binding.toolbar.title = it.displayName.toHtml()
            binding.profileHeader.setContent {
                ProfileHeader(user = it)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        adapter = ProfilePagerAdapter(this, viewModel.userId ?: 0)
        binding.viewPager.offscreenPageLimit = ProfileViewModel.ProfilePage.values().size
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(
                when (ProfileViewModel.ProfilePage.values()[position]) {
                    ProfileViewModel.ProfilePage.QUESTIONS -> R.string.questions
                    ProfileViewModel.ProfilePage.ANSWERS -> R.string.answers
                }
            )
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchUserData()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        viewModel.isCurrentUser.observe(this) {
            menu.findItem(R.id.hide).isVisible = !it
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressedDispatcher.onBackPressed()
            R.id.badges -> BadgesBottomSheetDialogFragment.show(
                supportFragmentManager,
                userId = viewModel.userId ?: -1
            )
            R.id.share -> viewModel.startShareIntent(this)
            R.id.hide -> {
                showDialog {
                    setIcon(R.drawable.ic_baseline_visibility_off)
                    setTitle(R.string.hide_user)
                    setMessage(R.string.hide_user_message)
                    setPositiveButton(R.string.hide) { _, _ ->
                        viewModel.hideUser()
                        finish()
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        internal const val USER_ID = "userId"
        private const val IS_FROM_DEEP_LINK = "isFromDeepLink"

        fun startActivity(
            context: Context,
            userId: Int,
            isFromDeepLink: Boolean = false,
        ) {
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra(USER_ID, userId)
                putExtra(IS_FROM_DEEP_LINK, isFromDeepLink)
            }
            context.startActivity(intent)
        }
    }
}
