package me.tylerbwong.stack.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import dev.chrisbanes.insetter.doOnApplyWindowInsets
import me.tylerbwong.stack.R
import me.tylerbwong.stack.ui.theme.ThemeManager

abstract class BaseActivity(
    @LayoutRes contentLayoutId: Int = 0
) : AppCompatActivity(contentLayoutId) {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.injectTheme(this)
        super.onCreate(savedInstanceState)
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
