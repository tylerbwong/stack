package me.tylerbwong.stack.api.service

import me.tylerbwong.stack.api.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {

    @GET("access-tokens/{accessToken}/invalidate")
    suspend fun logOut(
        @Path(ACCESS_TOKEN) accessToken: String,
        @Query(KEY_PARAM) key: String = BuildConfig.API_KEY
    )
}
