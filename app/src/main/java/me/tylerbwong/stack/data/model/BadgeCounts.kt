package me.tylerbwong.stack.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BadgeCounts(
    val bronze: Int,
    val silver: Int,
    val gold: Int
)
