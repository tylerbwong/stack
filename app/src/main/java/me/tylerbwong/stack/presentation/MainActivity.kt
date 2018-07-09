package me.tylerbwong.stack.presentation

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_main.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.R.id.drawerLayout
import me.tylerbwong.stack.R.id.searchView
import me.tylerbwong.stack.data.model.*
import me.tylerbwong.stack.presentation.questions.QuestionsFragment
import me.tylerbwong.stack.presentation.theme.ThemeManager
import timber.log.Timber

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        PopupMenu.OnMenuItemClickListener, SearchView.OnQueryTextListener {

    private var currentFragment: BaseFragment? = null
    private var menu: Menu? = null
    @Sort
    private var currentSort: String = CREATION

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.injectTheme(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ThemeManager.themeViews(toolbar)

        navigationView.setNavigationItemSelectedListener(this)

        val actionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        ) { /* No overrides needed */ }
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.isDrawerSlideAnimationEnabled = false
        actionBarDrawerToggle.isDrawerIndicatorEnabled = false
        actionBarDrawerToggle.toolbarNavigationClickListener = View.OnClickListener {
            if (drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START)
            } else {
                drawerLayout.openDrawer(Gravity.START)
            }
        }
        actionBarDrawerToggle.syncState()

        searchView.setOnQueryTextListener(this)

        navigationView.setCheckedItem(R.id.questions)
        QuestionsFragment.newInstance().also {
            setFragment(it)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()

        when (item.itemId) {
            R.id.questions -> {
                QuestionsFragment.newInstance()
            }
//            R.id.login -> {
//                LoginManager.startLogin(this)
//                currentFragment
//            }
            else -> {
                Timber.e("Could not resolve any fragment")
                null
            }
        }.also { setFragment(it) }

        return true
    }

    private fun setFragment(fragment: BaseFragment?) {
        fragment?.let {
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.contentFrame, it)
                    .commit()
            setFragmentTitle(it.titleRes)
            currentFragment = it
        }
    }

    private fun setFragmentTitle(@StringRes resId: Int?) {
        resId?.let {
            supportActionBar?.title = getString(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
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

    private fun sortQuestions(@Sort sort: String): Boolean {
        (currentFragment as? QuestionsFragment)?.sortQuestions(sort)
        currentSort = sort
        return true
    }

    private fun searchQuestions(query: String) =
            (currentFragment as? QuestionsFragment)?.searchQuestions(query)
}
