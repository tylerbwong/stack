package me.tylerbwong.stack.presentation

import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import me.tylerbwong.stack.R

abstract class BaseFragment : Fragment() {
    @StringRes
    open var titleRes = R.string.app_name
}