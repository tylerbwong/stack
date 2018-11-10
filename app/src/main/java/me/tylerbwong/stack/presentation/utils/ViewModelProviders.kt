package me.tylerbwong.stack.presentation.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

fun <T : ViewModel> Fragment.getViewModel(
        klazz: Class<T>
) = ViewModelProviders.of(this).get(klazz)

fun <T : ViewModel> FragmentActivity.getViewModel(
        klazz: Class<T>
) = ViewModelProviders.of(this).get(klazz)
