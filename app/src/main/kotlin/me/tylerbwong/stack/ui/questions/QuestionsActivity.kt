package me.tylerbwong.stack.ui.questions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.adapter.DynamicListAdapter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.api.model.ACTIVITY
import me.tylerbwong.stack.api.model.CREATION
import me.tylerbwong.stack.api.model.HOT
import me.tylerbwong.stack.api.model.MONTH
import me.tylerbwong.stack.api.model.VOTES
import me.tylerbwong.stack.api.model.WEEK
import me.tylerbwong.stack.databinding.ActivityQuestionsBinding
import me.tylerbwong.stack.ui.BaseActivity
import me.tylerbwong.stack.ui.utils.showSnackbar

@AndroidEntryPoint
class QuestionsActivity : BaseActivity<ActivityQuestionsBinding>(
    ActivityQuestionsBinding::inflate
), PopupMenu.OnMenuItemClickListener {

    private val viewModel by viewModels<QuestionsViewModel>()
    private val adapter = DynamicListAdapter(QuestionItemCallback)
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbar)

        val key = intent.getStringExtra(KEY_EXTRA) ?: ""

        if (key.isBlank()) {
            Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show()
            finish()
        }

        val page = intent.getSerializableExtra(PAGE_EXTRA) as QuestionPage

        setUpPageForKey(page, key)

        viewModel.refreshing.observe(this) {
            binding.refreshLayout.isRefreshing = it
        }

        viewModel.data.observe(this) {
            adapter.submitList(it)

            if (it.isEmpty()) {
                Snackbar.make(binding.rootLayout, R.string.nothing_here, Snackbar.LENGTH_INDEFINITE).show()
            }
        }

        binding.recyclerView.apply {
            adapter = this@QuestionsActivity.adapter
            layoutManager = LinearLayoutManager(context)
            applyInsetter {
                type(ime = true, statusBars = true, navigationBars = true) {
                    padding(bottom = true)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_sort_item, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.sort -> {
                PopupMenu(this, findViewById(R.id.sort)).also { popupMenu ->
                    popupMenu.inflate(R.menu.menu_sort)

                    if (!viewModel.isMainSortsSupported) {
                        listOf(R.id.hot, R.id.week, R.id.month).forEach {
                            popupMenu.menu?.removeItem(it)
                        }
                    }

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
        viewModel.getQuestions(sort = sort)
        return true
    }

    private fun setUpPageForKey(page: QuestionPage, key: String) {
        supportActionBar?.title = if (page.titleRes != null) {
            getString(page.titleRes)
        } else {
            key
        }

        viewModel.page = page
        viewModel.key = key

        viewModel.snackbar.observe(this) {
            if (it != null) {
                snackbar = binding.rootLayout.showSnackbar(
                    R.string.network_error,
                    R.string.retry
                ) { viewModel.getQuestions() }
            } else {
                snackbar?.dismiss()
            }
        }

        binding.refreshLayout.setOnRefreshListener { viewModel.getQuestions() }

        viewModel.getQuestions()
    }

    companion object {
        private const val PAGE_EXTRA = "page"
        private const val KEY_EXTRA = "key"

        fun makeIntentForKey(
            context: Context,
            page: QuestionPage,
            key: String,
            deepLinkSite: String? = null
        ) = Intent(context, QuestionsActivity::class.java)
            .putExtra(PAGE_EXTRA, page)
            .putExtra(KEY_EXTRA, key)
            .putExtra(DEEP_LINK_SITE, deepLinkSite)

        fun startActivityForKey(context: Context, page: QuestionPage, key: String) {
            context.startActivity(makeIntentForKey(context, page, key))
        }
    }
}
