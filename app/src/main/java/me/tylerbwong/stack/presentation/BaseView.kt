package me.tylerbwong.stack.presentation

interface BaseView<in T> {
    fun setPresenter(presenter: T)
}