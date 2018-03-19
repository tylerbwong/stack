package me.tylerbwong.stack.presentation

import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.NavigationView
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import me.tylerbwong.stack.R
import me.tylerbwong.stack.presentation.questions.QuestionsFragment
import timber.log.Timber

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
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
        QuestionsFragment.newInstance().also {
            setFragment(it)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawers()

        when(item.itemId) {
            R.id.questions -> {
                QuestionsFragment.newInstance()
            }
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
        }
    }

    private fun setFragmentTitle(@StringRes resId: Int?) {
        resId?.let {
            supportActionBar?.title = getString(it)
        }
    }
}
