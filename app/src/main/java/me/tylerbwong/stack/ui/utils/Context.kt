package me.tylerbwong.stack.ui.utils

import android.content.Context

inline fun <reified T : Any> Context.systemService(
    systemService: String
): T? = getSystemService(systemService) as? T
