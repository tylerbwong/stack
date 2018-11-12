package me.tylerbwong.stack.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.ACTIVITY
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.HOT
import me.tylerbwong.stack.data.model.MONTH
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.model.VOTES
import me.tylerbwong.stack.data.model.WEEK
import me.tylerbwong.stack.presentation.questions.QuestionsAdapter
import me.tylerbwong.stack.presentation.questions.QuestionsViewModel
import me.tylerbwong.stack.presentation.theme.ThemeManager
import me.tylerbwong.stack.presentation.utils.getViewModel

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener,
        SearchView.OnQueryTextListener {

    private lateinit var viewModel: QuestionsViewModel
    private val adapter = QuestionsAdapter()
    private var snackbar: Snackbar? = null

    @Sort
    private var currentSort: String = CREATION

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.injectTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = getViewModel(QuestionsViewModel::class.java)
        viewModel.refreshing.observe(this, Observer {
            refreshLayout?.isRefreshing = it
        })
        viewModel.snackbar.observe(this, Observer {
            if (it != null) {
                snackbar = Snackbar.make(refreshLayout, it, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.retry) { sortQuestions() }
                snackbar?.show()
            } else {
                snackbar?.dismiss()
            }
        })
        viewModel.questions.observe(this, Observer {
            adapter.questions = it
        })

        recyclerView.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                    ViewHolderItemDecoration(
                            context.resources.getDimensionPixelSize(R.dimen.item_spacing),
                            removeTopSpacing = true
                    )
            )
        }
        searchView.setOnQueryTextListener(this)

        refreshLayout.setOnRefreshListener { sortQuestions() }

        sortQuestions()
    }

    private fun sortQuestions(@Sort sort: String = CREATION): Boolean {
        viewModel.getQuestions(sort)
        currentSort = sort
        return true
    }

    private fun searchQuestions(query: String) = viewModel.searchQuestions(query)

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_questions, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.theme -> {
                ThemeManager.toggleTheme(this)
                recreate()
            }
            R.id.sort -> {
                PopupMenu(this, findViewById(R.id.sort)).also {
                    it.inflate(R.menu.menu_sort)
                    it.setOnMenuItemClickListener(this)
                    it.show()
                }
            }
            R.id.search -> {
                searchView.visibility = View.VISIBLE
                searchView.requestFocus()
                showKeyboard()
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (searchView.visibility == View.VISIBLE) {
            searchView.visibility = View.GONE
            sortQuestions(currentSort)
        } else {
            super.onBackPressed()
        }
    }

    override fun onMenuItemClick(item: MenuItem?) = when (item?.itemId) {
        R.id.creation -> sortQuestions(CREATION)
        R.id.activity -> sortQuestions(ACTIVITY)
        R.id.votes -> sortQuestions(VOTES)
        R.id.hot -> sortQuestions(HOT)
        R.id.week -> sortQuestions(WEEK)
        R.id.month -> sortQuestions(MONTH)
        else -> false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            hideKeyboard()
            searchQuestions(it)
            return true
        }
        return false
    }

    override fun onQueryTextChange(newText: String?) = false

    private fun hideKeyboard() {
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun showKeyboard() {
        currentFocus?.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(it, 0)
        }
    }
}
