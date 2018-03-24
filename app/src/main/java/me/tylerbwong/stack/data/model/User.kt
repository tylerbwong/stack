package me.tylerbwong.stack.data.model

import com.google.gson.annotations.SerializedName

/**
 * Shallow owner model.
 */
data class User(
        @SerializedName("accept_rate")
        val acceptRate: Int?,
        @SerializedName("display_name")
        val displayName: String,
        val link: String,
        @SerializedName("profile_image")
        val profileImage: String,
        val reputation: Int,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("user_type")
        val userType: String,
        @SerializedName("badge_counts")
        val badgeCounts: BadgeCounts
)