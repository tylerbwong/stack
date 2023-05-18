package me.tylerbwong.stack.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import dev.chrisbanes.insetter.applyInsetter
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.ui.theme.ThemeManager
import javax.inject.Inject

abstract class BaseActivity<T : ViewBinding>(
    // TODO Enable when Hilt supports default constructor arguments
    private val bindingProvider: ((LayoutInflater) -> T)? // = null
) : AppCompatActivity() {

    protected lateinit var binding: T

    protected open val isDefaultBackBehaviorEnabled = true

    @Inject
    lateinit var siteStore: SiteStore

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.injectTheme(this)
        super.onCreate(savedInstanceState)
        if (bindingProvider != null) {
            binding = bindingProvider.invoke(layoutInflater)
            setContentView(binding.root)
        }
        applyFullscreenWindowInsets()
        overrideDeepLinkSite()
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(isDefaultBackBehaviorEnabled) {
                override fun handleOnBackPressed() {
                    defaultOnBackPressed()
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        overrideDeepLinkSite()
    }

    @Suppress("deprecation") // TODO Update to Android R APIs
    protected open fun applyFullscreenWindowInsets() {
        val rootLayout = findViewById(R.id.rootLayout) ?: window.decorView
        rootLayout.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        val appBar = findViewById(R.id.appBar) ?: rootLayout
        appBar.applyInsetter {
            type(systemGestures = true) {
                padding(top = true)
            }
        }
    }

    protected fun defaultOnBackPressed(isSystemBack: Boolean = true) {
        val shouldBuildBackstack = intent.hasExtra(DEEP_LINK_SITE) && !isSystemBack
        if (shouldBuildBackstack && isTaskRoot && this@BaseActivity !is MainActivity) {
            startActivity(Intent(this@BaseActivity, MainActivity::class.java))
        }
        finish()
    }

    private fun overrideDeepLinkSite() {
        intent.getStringExtra(DEEP_LINK_SITE)?.let { siteStore.deepLinkSite = it }
    }

    companion object {
        internal const val DEEP_LINK_SITE = "deep_link_site"
    }
}
