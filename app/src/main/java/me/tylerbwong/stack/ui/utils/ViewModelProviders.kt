package me.tylerbwong.stack.ui.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

fun <T : ViewModel> FragmentActivity.getViewModel(
        klazz: Class<T>
) = ViewModelProviders.of(this).get(klazz)
