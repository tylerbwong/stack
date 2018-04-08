package me.tylerbwong.stack.presentation.questions.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.chip.Chip
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_question_detail.*
import kotlinx.android.synthetic.main.question_holder.*
import kotlinx.android.synthetic.main.user_view.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.Answer
import me.tylerbwong.stack.data.model.Question
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.presentation.ViewHolderItemDecoration
import me.tylerbwong.stack.presentation.answers.AnswerAdapter
import me.tylerbwong.stack.presentation.utils.GlideApp
import me.tylerbwong.stack.presentation.utils.format
import me.tylerbwong.stack.presentation.utils.setMarkdown
import me.tylerbwong.stack.presentation.utils.toHtml

class QuestionDetailActivity : AppCompatActivity(), QuestionDetailContract.View {

    private val presenter = QuestionDetailPresenter(this)
    private val adapter = AnswerAdapter()

    private var isFromDeepLink = false

    private lateinit var question: Question

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        recyclerView.apply {
            adapter = this@QuestionDetailActivity.adapter
            layoutManager = LinearLayoutManager(this@QuestionDetailActivity)
            addItemDecoration(
                    ViewHolderItemDecoration(
                            context.resources.getDimensionPixelSize(R.dimen.item_spacing),
                            true,
                            true
                    )
            )
            isNestedScrollingEnabled = false
        }

        isFromDeepLink = intent.getBooleanExtra(IS_FROM_DEEP_LINK, false)

        if (!isFromDeepLink) {
            questionTitle.text = intent.getStringExtra(QUESTION_TITLE).toHtml()
            questionBody.text = intent.getStringExtra(QUESTION_BODY).toHtml()
            bindUser(intent.getParcelableExtra(QUESTION_OWNER))
        }
        questionBody.setTextIsSelectable(true)

        presenter.questionId = intent.getIntExtra(QUESTION_ID, 0)

        refreshLayout.setOnRefreshListener { presenter.subscribe() }

        presenter.subscribe()
    }

    private fun bindUser(user: User?) {
        user?.let {
            username.text = it.displayName.toHtml()
            GlideApp.with(this)
                    .load(it.profileImage)
                    .placeholder(R.drawable.user_image_placeholder)
                    .apply(RequestOptions.circleCropTransform())
                    .into(userImage)
            badgeView.badgeCounts = it.badgeCounts
            reputation.text = it.reputation.toLong().format()
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.unsubscribe()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setQuestion(question: Question) {
        this.question = question
        val voteCount = question.upVoteCount - question.downVoteCount
        supportActionBar?.title = resources.getQuantityString(R.plurals.votes, voteCount, voteCount)
        questionBody.maxLines = Integer.MAX_VALUE
        questionBody.ellipsize = null
        answersCount.text = resources.getQuantityString(
                R.plurals.answers,
                question.answerCount,
                question.answerCount
        )

        question.bodyMarkdown?.let {
            questionBody.setMarkdown(it)
        }
        question.tags?.let {
            tagsView.removeAllViews()
            tagsView.visibility = View.VISIBLE
            it.forEach {
                tagsView.addView(Chip(this).apply {
                    chipText = it
                })
            }
        }

        if (isFromDeepLink) {
            questionTitle.text = question.title.toHtml()
            bindUser(question.owner)
        }
    }

    override fun setAnswers(answers: List<Answer>) {
        adapter.answers = answers
    }

    override fun setRefreshing(isRefreshing: Boolean) {
        if (refreshLayout?.isRefreshing != isRefreshing) {
            refreshLayout?.isRefreshing = isRefreshing
        }
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
