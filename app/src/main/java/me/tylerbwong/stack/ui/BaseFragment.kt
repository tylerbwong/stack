package me.tylerbwong.stack.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import me.tylerbwong.stack.ui.di.DaggerUiComponent

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApplicationWrapper.uiComponent.inject(this)
    }
}
