package me.tylerbwong.stack.data.billing

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val rawPrice: Long?,
    val formattedPrice: String?,
)
