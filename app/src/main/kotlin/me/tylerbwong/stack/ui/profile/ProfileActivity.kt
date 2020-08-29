package me.tylerbwong.stack.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import coil.api.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.ActivityProfileBinding
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.questions.QuestionItemCallback
import me.tylerbwong.stack.ui.utils.format
import me.tylerbwong.stack.ui.utils.launchCustomTab
import me.tylerbwong.stack.ui.utils.setSharedTransition
import me.tylerbwong.stack.ui.utils.setThrottledOnClickListener
import me.tylerbwong.stack.ui.utils.showSnackbar
import me.tylerbwong.stack.ui.utils.toHtml

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding>(ActivityProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()
    private val adapter = DynamicListAdapter(QuestionItemCallback)
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        setSharedTransition(
            android.R.id.statusBarBackground,
            android.R.id.navigationBarBackground
        )

        window.sharedElementEnterTransition = TransitionInflater.from(this)
            .inflateTransition(R.transition.shared_element_transition)

        viewModel.userId = intent.getIntExtra(USER_ID, 0)
        viewModel.refreshing.observe(this) {
            binding.refreshLayout.isRefreshing = it
        }
        viewModel.snackbar.observe(this) {
            if (it != null) {
                snackbar = binding.rootLayout.showSnackbar(R.string.network_error, R.string.retry) {
                    viewModel.getUserQuestionsAndAnswers()
                }
            } else {
                snackbar?.dismiss()
            }
        }

        viewModel.userData.observe(this) {
            val userImage = binding.includeProfileHeader.userImage
            userImage.load(it.profileImage) {
                crossfade(true)
                error(R.drawable.user_image_placeholder)
                placeholder(R.drawable.user_image_placeholder)
                transformations(CircleCropTransformation())
            }
            binding.collapsingToolbarLayout.title = it.displayName.toHtml()
            val location = binding.includeProfileHeader.location
            if (it.location != null) {
                location.text = it.location?.toHtml()
                location.isVisible = true
            } else {
                location.isGone = true
            }
            binding.includeProfileHeader.reputation.text = it.reputation.toLong().format()
            binding.includeProfileHeader.badgeView.badgeCounts = it.badgeCounts

            it.link?.let { link ->
                userImage.setThrottledOnClickListener {
                    launchCustomTab(this, link)
                }
            }
        }
        viewModel.questionsData.observe(this) {
            adapter.submitList(it)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.includeProfileHeader.userImage.transitionName =
            resources.getString(R.string.shared_transition_name)

        binding.recyclerView.apply {
            adapter = this@ProfileActivity.adapter
            layoutManager = LinearLayoutManager(this@ProfileActivity)
        }

        binding.refreshLayout.setOnRefreshListener { viewModel.getUserQuestionsAndAnswers() }

        viewModel.getUserQuestionsAndAnswers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.share -> viewModel.startShareIntent(this)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val USER_ID = "userId"
        private const val IS_FROM_DEEP_LINK = "isFromDeepLink"

        fun startActivity(
            context: Context,
            userId: Int,
            isFromDeepLink: Boolean = false,
            extras: Bundle? = null
        ) {
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra(USER_ID, userId)
                putExtra(IS_FROM_DEEP_LINK, isFromDeepLink)
            }
            context.startActivity(intent, extras)
        }
    }
}
