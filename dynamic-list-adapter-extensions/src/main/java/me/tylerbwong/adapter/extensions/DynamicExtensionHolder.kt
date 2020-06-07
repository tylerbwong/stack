package me.tylerbwong.adapter.extensions

import android.view.View
import kotlinx.android.extensions.LayoutContainer
import me.tylerbwong.adapter.DynamicHolder
import me.tylerbwong.adapter.DynamicItem

abstract class DynamicExtensionHolder<in T : DynamicItem>(
    rootView: View
) : DynamicHolder<T>(rootView), LayoutContainer
