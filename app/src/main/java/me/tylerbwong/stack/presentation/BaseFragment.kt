package me.tylerbwong.stack.presentation

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import me.tylerbwong.stack.R

abstract class BaseFragment : Fragment() {
    @StringRes
    open var titleRes = R.string.app_name
}
