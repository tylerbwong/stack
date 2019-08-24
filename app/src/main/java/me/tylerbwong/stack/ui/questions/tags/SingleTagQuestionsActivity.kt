package me.tylerbwong.stack.ui.questions.tags

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_single_tag_questions.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.ACTIVITY
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.HOT
import me.tylerbwong.stack.data.model.MONTH
import me.tylerbwong.stack.data.model.VOTES
import me.tylerbwong.stack.data.model.WEEK
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.DynamicViewAdapter
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration

class SingleTagQuestionsActivity : BaseActivity(), PopupMenu.OnMenuItemClickListener {

    private val viewModel by viewModels<SingleTagQuestionsViewModel>()
    private val adapter = DynamicViewAdapter()
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_tag_questions)
        setSupportActionBar(toolbar)

        val tag = intent.getStringExtra(TAG_EXTRA) ?: ""

        if (tag.isBlank()) {
            Toast.makeText(this, getString(R.string.single_tag_error, tag), Toast.LENGTH_LONG).show()
            finish()
        }

        supportActionBar?.title = tag

        viewModel.refreshing.observe(this) {
            refreshLayout.isRefreshing = it
        }
        viewModel.snackbar.observe(this) {
            if (it != null) {
                snackbar = Snackbar.make(
                        rootLayout,
                        getString(R.string.single_tag_error, tag),
                        Snackbar.LENGTH_INDEFINITE
                ).setAction(R.string.retry) { viewModel.getQuestionsByTag(tag) }
                snackbar?.show()
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.data.observe(this) {
            adapter.update(it)
        }

        recyclerView.apply {
            adapter = this@SingleTagQuestionsActivity.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                    ViewHolderItemDecoration(context.resources.getDimensionPixelSize(R.dimen.item_spacing_main))
            )
        }

        refreshLayout.setOnRefreshListener { viewModel.getQuestionsByTag(tag) }

        viewModel.getQuestionsByTag(tag)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sort_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.sort -> {
                PopupMenu(this, findViewById(R.id.sort)).also { popupMenu ->
                    popupMenu.inflate(R.menu.menu_sort)
                    popupMenu.setOnMenuItemClickListener(this)
                    popupMenu.show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val sort = when (item?.itemId) {
            R.id.creation -> CREATION
            R.id.activity -> ACTIVITY
            R.id.votes -> VOTES
            R.id.hot -> HOT
            R.id.week -> WEEK
            R.id.month -> MONTH
            else -> CREATION
        }
        viewModel.getQuestionsByTag(sort = sort)
        return true
    }

    companion object {
        private const val TAG_EXTRA = "tag"

        fun makeIntent(
                context: Context,
                tag: String
        ) = Intent(context, SingleTagQuestionsActivity::class.java).putExtra(TAG_EXTRA, tag)

        fun startActivity(context: Context, tag: String) {
            context.startActivity(makeIntent(context, tag))
        }
    }
}
