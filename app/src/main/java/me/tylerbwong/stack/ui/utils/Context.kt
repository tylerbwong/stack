package me.tylerbwong.stack.ui.utils

import android.content.Context
import android.content.ContextWrapper

inline fun <reified T : Any> Context.systemService(
    systemService: String
): T? = getSystemService(systemService) as? T

inline fun <reified T : Context> Context.ofType(): T? {
    var currentContext: Context? = this
    do {
        if (currentContext is T) {
            return currentContext
        }

        currentContext = (currentContext as? ContextWrapper)?.baseContext
    } while(currentContext != null)
    return null
}
