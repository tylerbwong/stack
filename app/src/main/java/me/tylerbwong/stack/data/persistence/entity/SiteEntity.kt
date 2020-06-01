package me.tylerbwong.stack.data.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "site")
data class SiteEntity(
    val name: String,
    @PrimaryKey
    val parameter: String,
    val url: String,
    val audience: String,
    val iconUrl: String
)
