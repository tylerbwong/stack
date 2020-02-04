package me.tylerbwong.stack.data.auth

import android.net.Uri
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import me.tylerbwong.stack.BaseTest
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutError
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutSuccess
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.data.network.service.AuthService
import me.tylerbwong.stack.data.network.service.UserService
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.UserDao
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import retrofit2.HttpException
import retrofit2.Response

class AuthRepositoryTest : BaseTest() {

    @Mock
    private lateinit var answerDraftDao: AnswerDraftDao

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var authService: AuthService

    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        repository = AuthRepository(answerDraftDao, userService, authService)
    }

    @Test
    fun `logOut with existing access token returns success state`() {
        runBlocking {
            AuthStore.setAccessToken(testUri)
            val result = repository.logOut()
            assertTrue(result is LogOutSuccess)
            verify(authService).logOut("test")
            verify(answerDraftDao).clearDrafts()
            assertTrue(AuthStore.accessToken.isNullOrBlank())
        }
    }

    @Test
    fun `logOut with absent access token returns error state`() {
        runBlocking {
            AuthStore.clear()
            val result = repository.logOut()
            assertTrue(result is LogOutError)
            verify(authService, never()).logOut(any(), any())
            verify(answerDraftDao, never()).clearDrafts()
        }
    }

    @Test
    fun `logOut that comes back with error returns error state`() {
        runBlocking {
            AuthStore.setAccessToken(testUri)
            whenever(authService.logOut(any(), any()))
                .thenThrow(
                    HttpException(
                        Response.error<ResponseBody>(
                            403, """
                        {
                            "errorCode": 403
                        }
                    """.trimIndent().toResponseBody("application/json".toMediaTypeOrNull())
                        )
                    )
                )
            val result = repository.logOut()
            assertTrue(result is LogOutError)
        }
    }

    @Test
    fun `getCurrentUser with existing access token makes service and db calls`() {
        runBlocking {
            whenever(userService.getCurrentUser(any(), any(), any())).thenReturn(
                StackResponse(listOf(testUser), false)
            )
            AuthStore.setAccessToken(testUri)
            assertEquals(testUser, repository.getCurrentUser())
            verify(userService).getCurrentUser(any(), any(), any())
//            verify(userDao).insert(any())
        }
    }

    @Test
    fun `getCurrentUser with absent access token never makes service and db calls and returns null`() {
        runBlocking {
            AuthStore.clear()
            assertNull(repository.getCurrentUser())
            verify(userService, never()).getCurrentUser(any(), any(), any())
            verify(userDao, never()).insert(any())
        }
    }

    @Test
    fun `getCurrentUser with throwing service call returns null`() {
        runBlocking {
            whenever(userService.getCurrentUser(any(), any(), any()))
                .thenThrow(
                    HttpException(
                        Response.error<ResponseBody>(
                            403, """
                        {
                            "errorCode": 403
                        }
                    """.trimIndent().toResponseBody("application/json".toMediaTypeOrNull())
                        )
                    )
                )
            AuthStore.setAccessToken(testUri)
            assertNull(repository.getCurrentUser())
            verify(userService).getCurrentUser(any(), any(), any())
            verify(userDao, never()).insert(any())
        }
    }

    @Test
    @Ignore("AuthRepository no longer stores the current user in the db")
    fun `getCurrentUser with throwing db call still returns the user from the service`() {
        runBlocking {
            whenever(userService.getCurrentUser(any(), any(), any())).thenReturn(
                StackResponse(listOf(testUser), false)
            )
            whenever(userDao.insert(any())).thenThrow(IllegalStateException("Could not insert"))
            AuthStore.setAccessToken(testUri)
            assertEquals(testUser, repository.getCurrentUser())
            verify(userService).getCurrentUser(any(), any(), any())
//            verify(userDao).insert(any())
        }
    }

    companion object {
        private val testUri = Uri.parse("https://test.com/auth#access_token=test")
        private val testUser = User(
            null,
            null,
            null,
            "test_user",
            null,
            null,
            null,
            0,
            0,
            "registered",
            null
        )
    }
}
