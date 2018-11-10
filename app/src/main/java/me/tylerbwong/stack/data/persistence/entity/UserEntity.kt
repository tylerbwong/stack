package me.tylerbwong.stack.data.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
        val acceptRate: Int?,
        val displayName: String,
        val link: String?,
        val profileImage: String?,
        val reputation: Int,
        @PrimaryKey val userId: Int,
        val userType: String,
        val bronzeBadgeCount: Int,
        val silverBadgeCount: Int,
        val goldBadgeCount: Int
)
