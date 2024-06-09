package me.tylerbwong.stack.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Site(
    @SerialName("name")
    val name: String,
    @SerialName("api_site_parameter")
    val parameter: String,
    @SerialName("site_url")
    val url: String,
    @SerialName("audience")
    val audience: String,
    @SerialName("icon_url")
    val iconUrl: String
) {
    var isUserRegistered: Boolean = false
}
