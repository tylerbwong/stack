package me.tylerbwong.stack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
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
    }

    override fun onResume() {
        super.onResume()
        overrideDeepLinkSite()
    }

    @Suppress("deprecation") // TODO Update to Android R APIs
    @CallSuper
    protected open fun applyFullscreenWindowInsets() {
        val rootLayout = findViewById<View>(R.id.rootLayout)
        rootLayout?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        val appBar = findViewById(R.id.appBar) ?: rootLayout
        appBar?.applyInsetter {
            type(systemGestures = true) {
                padding(top = true)
            }
        }
    }

    private fun overrideDeepLinkSite() {
        intent.getStringExtra(DEEP_LINK_SITE)?.let { siteStore.deepLinkSite = it }
    }

    companion object {
        internal const val DEEP_LINK_SITE = "deep_link_site"
    }
}
