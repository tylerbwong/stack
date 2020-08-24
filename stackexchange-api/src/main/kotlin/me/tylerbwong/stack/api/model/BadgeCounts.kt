package me.tylerbwong.stack.api.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BadgeCounts(
    val bronze: Int,
    val silver: Int,
    val gold: Int
)
