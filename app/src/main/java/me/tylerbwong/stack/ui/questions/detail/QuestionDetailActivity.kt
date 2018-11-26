package me.tylerbwong.stack.ui.questions.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_question_detail.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.ui.theme.ThemeManager
import me.tylerbwong.stack.ui.utils.DynamicViewAdapter
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.getViewModel

class QuestionDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: QuestionDetailViewModel
    private val adapter = DynamicViewAdapter()
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.injectTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        setSupportActionBar(toolbar)

        viewModel = getViewModel(QuestionDetailViewModel::class.java)
        viewModel.refreshing.observe(this, Observer {
            refreshLayout?.isRefreshing = it
        })
        viewModel.snackbar.observe(this, Observer {
            if (it != null) {
                snackbar = Snackbar.make(rootLayout, it, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) { viewModel.getQuestionDetails() }
                snackbar?.show()
            } else {
                snackbar?.dismiss()
            }
        })
        viewModel.data.observe(this, Observer {
            adapter.update(it)
        })
        viewModel.voteCount.observe(this, Observer {
            supportActionBar?.title = resources.getQuantityString(R.plurals.votes, it, it)
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        recyclerView.apply {
            adapter = this@QuestionDetailActivity.adapter
            layoutManager = LinearLayoutManager(this@QuestionDetailActivity)
            addItemDecoration(
                    ViewHolderItemDecoration(
                            context.resources.getDimensionPixelSize(R.dimen.item_spacing_question_detail),
                            true,
                            true
                    )
            )
            isNestedScrollingEnabled = false
        }

        viewModel.isFromDeepLink = intent.getBooleanExtra(IS_FROM_DEEP_LINK, false)

        viewModel.questionId = intent.getIntExtra(QUESTION_ID, 0)

        refreshLayout.setOnRefreshListener { viewModel.getQuestionDetails() }

        viewModel.getQuestionDetails()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_question_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.share -> viewModel.startShareIntent(this)
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val QUESTION_ID = "id"
        private const val QUESTION_TITLE = "title"
        private const val QUESTION_BODY = "body"
        private const val QUESTION_OWNER = "owner"
        private const val IS_FROM_DEEP_LINK = "isFromDeepLink"

        fun startActivity(
                context: Context,
                id: Int,
                title: String? = null,
                body: String? = null,
                owner: User? = null,
                isFromDeepLink: Boolean = false
        ) {
            val intent = Intent(context, QuestionDetailActivity::class.java).apply {
                putExtra(QUESTION_ID, id)
                putExtra(QUESTION_TITLE, title)
                putExtra(QUESTION_BODY, body)
                putExtra(QUESTION_OWNER, owner)
                putExtra(IS_FROM_DEEP_LINK, isFromDeepLink)
            }
            context.startActivity(intent)
        }
    }
}
