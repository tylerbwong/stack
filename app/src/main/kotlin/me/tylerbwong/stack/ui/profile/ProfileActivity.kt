package me.tylerbwong.stack.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.databinding.ActivityProfileBinding
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.questions.QuestionItemCallback
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
            binding.collapsingToolbarLayout.title = it.displayName.toHtml()
            binding.profileHeader.setContent {
                ProfileHeader(user = it)
            }
        }
        viewModel.questionsData.observe(this) {
            adapter.submitList(it)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.recyclerView.apply {
            adapter = this@ProfileActivity.adapter
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            applyInsetter {
                type(ime = true, statusBars = true, navigationBars = true) {
                    padding(bottom = true)
                }
            }
        }

        binding.refreshLayout.setOnRefreshListener { viewModel.getUserQuestionsAndAnswers() }

        viewModel.getUserQuestionsAndAnswers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.badges -> BadgesBottomSheetDialogFragment.show(
                supportFragmentManager,
                userId = viewModel.userId ?: -1
            )
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
        ) {
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra(USER_ID, userId)
                putExtra(IS_FROM_DEEP_LINK, isFromDeepLink)
            }
            context.startActivity(intent)
        }
    }
}
