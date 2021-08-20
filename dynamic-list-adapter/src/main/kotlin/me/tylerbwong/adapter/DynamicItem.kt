package me.tylerbwong.adapter

@Suppress("UnnecessaryAbstractClass")
abstract class DynamicItem(val viewHolderProvider: ViewHolderProvider) {
    open val itemId = this::class.java.name.hashCode()
}
