package me.tylerbwong.stack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.theme.ThemeManager

abstract class BaseActivity<T : ViewBinding>(
    private val bindingProvider: ((LayoutInflater) -> T)? = null
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

    @CallSuper
    protected open fun applyFullscreenWindowInsets() {
        findViewById<View>(R.id.rootLayout)?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        findViewById<View>(R.id.appBar)?.doOnApplyWindowInsets { view, insets, initialState ->
            view.updatePadding(
                top = initialState.paddings.top + insets.systemWindowInsetTop
            )
        }
    }
}
