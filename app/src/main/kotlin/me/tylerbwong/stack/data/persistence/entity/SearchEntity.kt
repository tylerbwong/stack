package me.tylerbwong.stack.data.persistence.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search")
data class SearchEntity(
    val query: String,
    val isAccepted: Boolean?,
    val minNumAnswers: Int?,
    val bodyContains: String?,
    val isClosed: Boolean?,
    val tags: String?,
    val titleContains: String?,
    val site: String
) {
    @PrimaryKey
    var queryKey = hashCode()

    var timestamp = System.currentTimeMillis()
}
