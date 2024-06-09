package me.tylerbwong.stack.api.model

import androidx.annotation.StringDef
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

const val NAMED = "named"
const val TAG_BASED = "tag_based"

@StringDef(NAMED, TAG_BASED)
annotation class BadgeType

const val GOLD = "gold"
const val SILVER = "silver"
const val BRONZE = "bronze"

@StringDef(GOLD, SILVER, BRONZE)
annotation class Rank

@Serializable
data class Badge(
    @SerialName("award_count")
    val awardCount: Int,
    @SerialName("badge_id")
    val badgeId: Int,
    @[SerialName("badge_type") BadgeType]
    val badgeType: String,
    @SerialName("description")
    val description: String,
    @SerialName("link")
    val link: String,
    @SerialName("name")
    val name: String,
    @[SerialName("rank") Rank]
    val rank: String
)
