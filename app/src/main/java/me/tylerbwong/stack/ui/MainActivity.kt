package me.tylerbwong.stack.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.InstallStatus
import kotlinx.android.synthetic.main.activity_main.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.AppUpdater
import me.tylerbwong.stack.data.model.ACTIVITY
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.HOT
import me.tylerbwong.stack.data.model.MONTH
import me.tylerbwong.stack.data.model.VOTES
import me.tylerbwong.stack.data.model.WEEK
import me.tylerbwong.stack.ui.questions.HeaderDataModel
import me.tylerbwong.stack.ui.questions.QuestionDataModel
import me.tylerbwong.stack.ui.questions.QuestionsViewModel
import me.tylerbwong.stack.ui.theme.ThemeManager
import me.tylerbwong.stack.ui.utils.DynamicDataModel
import me.tylerbwong.stack.ui.utils.DynamicViewAdapter
import me.tylerbwong.stack.ui.utils.ViewHolderItemDecoration
import me.tylerbwong.stack.ui.utils.showSnackbar

class MainActivity : BaseActivity(), PopupMenu.OnMenuItemClickListener,
        SearchView.OnQueryTextListener, InstallStateUpdatedListener {

    private val viewModel: QuestionsViewModel by viewModels()
    private val adapter = DynamicViewAdapter()
    private var snackbar: Snackbar? = null
    private var menu: Menu? = null

    private lateinit var appUpdater: AppUpdater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar?.title = ""

        viewModel.refreshing.observe(this) {
            refreshLayout.isRefreshing = it
        }
        viewModel.snackbar.observe(this) {
            if (it != null) {
                snackbar = rootLayout.showSnackbar(R.string.network_error, R.string.retry) {
                    viewModel.getQuestions()
                }
            } else {
                snackbar?.dismiss()
            }
        }
        viewModel.questions.observe(this) {
            updateContent(it)
        }

        recyclerView.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(
                    ViewHolderItemDecoration(context.resources.getDimensionPixelSize(R.dimen.item_spacing_main))
            )
        }
        searchView.setOnQueryTextListener(this)
        searchView.findViewById<ImageView>(R.id.search_close_btn)?.setOnClickListener {
            clearSearch()
        }

        refreshLayout.setOnRefreshListener { viewModel.fetchQuestions() }

        viewModel.fetchQuestions()

        appUpdater = AppUpdater(AppUpdateManagerFactory.create(this))
        appUpdater.checkForUpdate(this)
    }

    override fun onResume() {
        super.onResume()
        checkForPendingInstall()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.menu_questions, menu)
        menuInflater.inflate(R.menu.menu_sort_item, menu)

        if (!viewModel.isQueryBlank()) {
            menu.findItem(R.id.search)?.let {
                onOptionsItemSelected(it)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
            viewModel.currentQuery = ""
            viewModel.getQuestions()
        } else {
            super.onBackPressed()
        }
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
        clearSearch(fetchQuestions = false)
        viewModel.getQuestions(sort)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            hideKeyboard()
            viewModel.searchQuestions(it)
            return true
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.onQueryTextChange(newText)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUpdater.APP_UPDATE_REQUEST_CODE) {
            when (resultCode) {
                RESULT_OK -> checkForPendingInstall()
                else -> {
                    rootLayout.showSnackbar(
                            R.string.update_not_downloaded,
                            R.string.update,
                            Snackbar.LENGTH_LONG
                    ) { appUpdater.checkForUpdate(this) }
                }
            }
        }
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            checkForPendingInstall()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdater.unregisterListener(this)
    }

    private fun checkForPendingInstall() {
        appUpdater.checkForPendingInstall { manager ->
            rootLayout.showSnackbar(R.string.restart_to_install, R.string.restart) {
                manager.completeUpdate()
                appUpdater.unregisterListener(this)
            }
        }
    }

    private fun clearSearch(fetchQuestions: Boolean = true) {
        searchView.setQuery("", false)
        searchView.visibility = View.GONE

        if (fetchQuestions) {
            viewModel.fetchQuestions()
        }
    }

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

    private fun updateContent(questions: List<QuestionDataModel>) {
        val content = questions.toMutableList<DynamicDataModel>().apply {
            val subtitle: String = when {
                !viewModel.isQueryBlank() -> "\"${viewModel.currentQuery}\""
                else -> viewModel.currentSort.toLowerCase().capitalize()
            }
            add(0, HeaderDataModel(getString(R.string.questions), subtitle))
        }
        adapter.update(content)
    }
}
