package me.tylerbwong.stack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import dev.chrisbanes.insetter.applySystemGestureInsetsToPadding
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.theme.ThemeManager

abstract class BaseActivity<T : ViewBinding>(
    // TODO Enable when Hilt supports default constructor arguments
    private val bindingProvider: ((LayoutInflater) -> T)? // = null
) : AppCompatActivity() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.injectTheme(this)
        super.onCreate(savedInstanceState)
        if (bindingProvider != null) {
            binding = bindingProvider.invoke(layoutInflater)
            setContentView(binding.root)
        }
        applyFullscreenWindowInsets()
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
}
