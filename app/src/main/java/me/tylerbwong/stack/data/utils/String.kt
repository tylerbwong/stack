package me.tylerbwong.stack.data.utils

fun String.replaceAll(oldValues: List<String>, newValue: String): String {
    var result = this
    oldValues.forEach {
        result = result.replace(it, newValue)
    }
    return result
}
