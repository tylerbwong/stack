package me.tylerbwong.stack.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.tylerbwong.stack.ui.theme.ThemeManager

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeManager.injectTheme(this)
        super.onCreate(savedInstanceState)
    }
}
