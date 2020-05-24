package me.tylerbwong.stack.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchPayload(
    val query: String,
    val isAccepted: Boolean? = null,
    val minNumAnswers: Int? = null,
    val bodyContains: String? = null,
    val isClosed: Boolean? = null,
    val tags: List<String>? = null,
    val titleContains: String? = null
) : Parcelable {
    fun isNotEmpty() = query.isNotEmpty() || listOf(
        isAccepted,
        minNumAnswers,
        bodyContains,
        isClosed,
        tags,
        titleContains
    ).any { it != null }
}
