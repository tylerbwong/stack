package me.tylerbwong.stack.data.network.service

import me.tylerbwong.stack.data.model.Comment
import me.tylerbwong.stack.data.model.ORDER_PARAM
import me.tylerbwong.stack.data.model.Order
import me.tylerbwong.stack.data.model.Response
import me.tylerbwong.stack.data.network.ServiceProvider
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CommentService {

    @GET("posts/{id}/comments")
    suspend fun getPostComments(
        @Path("id") postId: Int,
        @Query(SITE_PARAM) site: String = DEFAULT_SITE,
        @Query(ORDER_PARAM) @Order order: String = DEFAULT_ORDER,
        @Query(PAGE_SIZE_PARAM) pageSize: Int = DEFAULT_PAGE_SIZE,
        @Query(PAGE_PARAM) page: Int = DEFAULT_PAGE,
        @Query(FILTER_PARAM) filter: String = COMMENTS_FILTER,
        @Query(KEY_PARAM) key: String = ServiceProvider.DEFAULT_KEY
    ): Response<Comment>

    companion object {
        private const val COMMENTS_FILTER =
            "!)pfJrS0ZPMUmIMpNO)Yke6OecqSjv9ILyIMzhIz6XW5PAELLlGsc(-cn4aL4Utv5ur"
    }
}
