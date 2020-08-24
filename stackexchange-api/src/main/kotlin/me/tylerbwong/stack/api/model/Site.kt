package me.tylerbwong.stack.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Site(
    @Json(name = "name")
    val name: String,
    @Json(name = "api_site_parameter")
    val parameter: String,
    @Json(name = "site_url")
    val url: String,
    @Json(name = "audience")
    val audience: String,
    @Json(name = "icon_url")
    val iconUrl: String
)
