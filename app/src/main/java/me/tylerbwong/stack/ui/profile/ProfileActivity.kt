package me.tylerbwong.stack.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_profile.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.DynamicDataModel
import me.tylerbwong.stack.ui.utils.DynamicViewAdapter
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.showSnackbar

class ProfileActivity : BaseActivity() {

    private val viewModel: ProfileViewModel by viewModels()
    private val adapter = DynamicViewAdapter()
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        viewModel.userId = intent.getIntExtra(USER_ID, 0)
        viewModel.refreshing.observe(this) {
            refreshLayout.isRefreshing = it
        }
        viewModel.snackbar.observe(this) {
            if (it != null) {
                snackbar = rootLayout.showSnackbar(R.string.network_error, R.string.retry) {
                    viewModel.getUserQuestionsAndAnswers()
                }
            } else {
                snackbar?.dismiss()
            }
        }
        // TODO(Tyler) Integrate getUser endpoint
        // For now, we pull a user's answers and questions in case they don't have any of either
        // and we can't get their user info
        viewModel.answersData.observe(this) {
            val data = mutableListOf<DynamicDataModel>()
            it.firstOrNull()?.owner?.let {  user ->
                supportActionBar?.title = user.displayName
                data.add(0, ProfileHeaderDataModel(user))
            }
            adapter.update(data)
        }
        viewModel.questionsData.observe(this) {
            val data = it.toMutableList<DynamicDataModel>()
            it.firstOrNull()?.owner?.let {  user ->
                supportActionBar?.title = user.displayName
                data.add(0, ProfileHeaderDataModel(user))
            }
            adapter.update(data)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        recyclerView.apply {
            adapter = this@ProfileActivity.adapter
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            addItemDecoration(
                    ViewHolderItemDecoration(
                            context.resources.getDimensionPixelSize(R.dimen.item_spacing_main)
                    )
            )
        }

        refreshLayout.setOnRefreshListener { viewModel.getUserQuestionsAndAnswers() }

        viewModel.getUserQuestionsAndAnswers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
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
                isFromDeepLink: Boolean = false
        ) {
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra(USER_ID, userId)
                putExtra(IS_FROM_DEEP_LINK, isFromDeepLink)
            }
            context.startActivity(intent)
        }
    }
}
