package me.tylerbwong.stack.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BadgeCounts(
    val bronze: Int,
    val silver: Int,
    val gold: Int
) : Parcelable
