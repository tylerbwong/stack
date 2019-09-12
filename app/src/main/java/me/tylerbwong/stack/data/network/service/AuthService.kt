package me.tylerbwong.stack.data.network.service

import me.tylerbwong.stack.data.model.Response
import me.tylerbwong.stack.data.network.ServiceProvider
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {

    @GET("apps/{accessToken}/de-authenticate")
    suspend fun logOut(
        @Path(ACCESS_TOKEN) accessToken: String,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Unit>
}
