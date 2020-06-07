package me.tylerbwong.adapter

@Suppress("UnnecessaryAbstractClass")
abstract class DynamicItem(val viewHolderProvider: ViewHolderProvider) {
    internal val itemId = this::class.java.name.hashCode()
}
