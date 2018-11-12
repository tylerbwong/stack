package me.tylerbwong.stack.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.model.ACTIVITY
import me.tylerbwong.stack.data.model.CREATION
import me.tylerbwong.stack.data.model.HOT
import me.tylerbwong.stack.data.model.MONTH
import me.tylerbwong.stack.data.model.Sort
import me.tylerbwong.stack.data.model.VOTES
import me.tylerbwong.stack.data.model.WEEK
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
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
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
