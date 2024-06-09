package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Shallow owner model.
 */
@Serializable
data class User(
    @SerialName("about_me")
    val aboutMe: String?,
    @SerialName("accept_rate")
    val acceptRate: Int?,
    @SerialName("account_id")
    val accountId: Int?,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("link")
    val link: String?,
    @SerialName("location")
    val location: String?,
    @SerialName("profile_image")
    val profileImage: String?,
    @SerialName("reputation")
    val reputation: Int = 0,
    @SerialName("user_id")
    val userId: Int = 0,
    @SerialName("user_type")
    val userType: String,
    @SerialName("badge_counts")
    val badgeCounts: BadgeCounts?
)
