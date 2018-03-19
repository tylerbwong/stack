package me.tylerbwong.stack.presentation

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.presentation.questions.QuestionsFragment

class MainActivity : AppCompatActivity(), BaseView<MainPresenter>,
        NavigationView.OnNavigationItemSelectedListener {

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
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
        actionBarDrawerToggle.syncState()

        navigationView.setCheckedItem(R.id.questions)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun setPresenter(presenter: MainPresenter) {
        this.presenter = presenter
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()

        setFragment(when(item.itemId) {
            R.id.questions -> {
                supportActionBar?.title = getString(R.string.questions)
                QuestionsFragment.newInstance()
            }
            else -> {
                Log.e("ERROR", "Could not resolve any fragment")
                null
            }
        })

        return true
    }

    private fun setFragment(fragment: Fragment?) {
        fragment?.let {
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.contentFrame, it)
                    .commit()
        }
    }
}
