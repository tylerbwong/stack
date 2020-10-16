package me.tylerbwong.stack.api.model

import androidx.annotation.StringDef
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

const val NAMED = "named"
const val TAG_BASED = "tag_based"

@StringDef(NAMED, TAG_BASED)
annotation class BadgeType

const val GOLD = "gold"
const val SILVER = "silver"
const val BRONZE = "bronze"

@StringDef(GOLD, SILVER, BRONZE)
annotation class Rank

@JsonClass(generateAdapter = true)
data class Badge(
    @Json(name = "award_count")
    val awardCount: Int,
    @Json(name = "badge_id")
    val badgeId: Int,
    @[Json(name = "badge_type") BadgeType]
    val badgeType: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "link")
    val link: String,
    @Json(name = "name")
    val name: String,
    @[Json(name = "rank") Rank]
    val rank: String
)
