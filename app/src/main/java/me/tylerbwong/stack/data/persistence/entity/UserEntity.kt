package me.tylerbwong.stack.data.persistence.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

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