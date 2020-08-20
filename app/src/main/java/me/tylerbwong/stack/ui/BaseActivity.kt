package me.tylerbwong.stack.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.viewbinding.ViewBinding
import dev.chrisbanes.insetter.Insetter
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

    @CallSuper
    protected open fun applyFullscreenWindowInsets() {
        val rootLayout = findViewById<View>(R.id.rootLayout)
        rootLayout?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        val appBar = findViewById(R.id.appBar) ?: rootLayout
        if (appBar != null) {
            Insetter.builder().setOnApplyInsetsListener { view, insets, initialState ->
                view.updatePadding(
                    top = initialState.paddings.top + insets.getInsets(
                        WindowInsetsCompat.Type.systemBars()
                    ).top
                )
            }.applyToView(appBar)
        }
    }
}
