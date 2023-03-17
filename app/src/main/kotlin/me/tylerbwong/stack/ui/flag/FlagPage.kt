package me.tylerbwong.stack.ui.flag

sealed class FlagPage {
    object Options : FlagPage()
    object Comment : FlagPage()
    object Duplicate : FlagPage()
}
