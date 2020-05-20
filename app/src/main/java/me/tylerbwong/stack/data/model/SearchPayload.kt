package me.tylerbwong.stack.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class SearchPayload {

    @Parcelize
    data class Standard(
        val query: String,
        val isAccepted: Boolean? = null,
        val minNumAnswers: Int? = null,
        val bodyContains: String? = null,
        val isClosed: Boolean? = null,
        val tags: List<String>? = null,
        val titleContains: String? = null,
        val searchId: Int? = null
    ) : SearchPayload(), Parcelable

    object Empty : SearchPayload()
}
