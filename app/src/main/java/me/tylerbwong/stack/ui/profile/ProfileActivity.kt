package me.tylerbwong.stack.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_profile.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.DynamicViewAdapter
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.getViewModel

class ProfileActivity : BaseActivity() {

    private lateinit var viewModel: ProfileViewModel
    private val adapter = DynamicViewAdapter()
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(toolbar)

        viewModel = getViewModel(ProfileViewModel::class.java)
        viewModel.userId = intent.getIntExtra(USER_ID, 0)
        viewModel.refreshing.observe(this, Observer {
            refreshLayout?.isRefreshing = it
        })
        viewModel.snackbar.observe(this, Observer {
            if (it != null) {
                snackbar = Snackbar.make(rootLayout, it, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) { viewModel.getUserQuestionsAndAnswers() }
                snackbar?.show()
            } else {
                snackbar?.dismiss()
            }
        })
        viewModel.answersData.observe(this, Observer {
            adapter.update(it)
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        recyclerView.apply {
            adapter = this@ProfileActivity.adapter
            layoutManager = LinearLayoutManager(this@ProfileActivity)
            addItemDecoration(
                    ViewHolderItemDecoration(
                            context.resources.getDimensionPixelSize(R.dimen.item_spacing_question_detail)
                    )
            )
        }

        refreshLayout.setOnRefreshListener { viewModel.getUserQuestionsAndAnswers() }

        viewModel.getUserQuestionsAndAnswers()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
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
