package me.tylerbwong.stack.api.model

import kotlinx.serialization.Serializable

@Serializable
data class BadgeCounts(
    val bronze: Int,
    val silver: Int,
    val gold: Int
)
