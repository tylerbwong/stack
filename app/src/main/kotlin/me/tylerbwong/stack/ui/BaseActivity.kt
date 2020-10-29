package me.tylerbwong.stack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import dev.chrisbanes.insetter.applySystemGestureInsetsToPadding
import me.tylerbwong.stack.R
import me.tylerbwong.stack.data.site.SiteStore
import me.tylerbwong.stack.ui.theme.ThemeManager
import me.tylerbwong.stack.ui.utils.ConnectivityChecker
import me.tylerbwong.stack.ui.utils.createConnectivitySnackbar
import javax.inject.Inject

abstract class BaseActivity<T : ViewBinding>(
    // TODO Enable when Hilt supports default constructor arguments
    private val bindingProvider: ((LayoutInflater) -> T)? // = null
) : AppCompatActivity() {

    protected lateinit var binding: T

    @Inject
    lateinit var siteStore: SiteStore

    @[Inject JvmField]
    var connectivityChecker: ConnectivityChecker? = null

    private var networkSnackbar: Snackbar? = null

    protected open val anchorView: View
        get() = binding.root

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.injectTheme(this)
        super.onCreate(savedInstanceState)
        if (bindingProvider != null) {
            binding = bindingProvider.invoke(layoutInflater)
            setContentView(binding.root)
        }
        applyFullscreenWindowInsets()
        overrideDeepLinkSite()
        connectivityChecker?.let {
            lifecycle.addObserver(it)
            it.connectedState.observe(this) { isConnected ->
                if (!isConnected) {
                    if (networkSnackbar == null) {
                        networkSnackbar = createConnectivitySnackbar(anchorView)
                    }
                    networkSnackbar?.show()
                } else {
                    networkSnackbar?.dismiss()
                }
            }
        }
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
        appBar?.applySystemGestureInsetsToPadding(top = true)
    }

    private fun overrideDeepLinkSite() {
        intent.getStringExtra(DEEP_LINK_SITE)?.let { siteStore.deepLinkSite = it }
    }

    companion object {
        internal const val DEEP_LINK_SITE = "deep_link_site"
    }
}
