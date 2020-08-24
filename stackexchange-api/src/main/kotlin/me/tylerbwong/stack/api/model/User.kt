package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Shallow owner model.
 */
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "about_me")
    val aboutMe: String?,
    @Json(name = "accept_rate")
    val acceptRate: Int?,
    @Json(name = "account_id")
    val accountId: Int?,
    @Json(name = "display_name")
    val displayName: String,
    @Json(name = "link")
    val link: String?,
    @Json(name = "location")
    val location: String?,
    @Json(name = "profile_image")
    val profileImage: String?,
    @Json(name = "reputation")
    val reputation: Int = 0,
    @Json(name = "user_id")
    val userId: Int = 0,
    @Json(name = "user_type")
    val userType: String,
    @Json(name = "badge_counts")
    val badgeCounts: BadgeCounts?
)
