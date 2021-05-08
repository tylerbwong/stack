package me.tylerbwong.stack.data.auth

import android.net.Uri
import kotlinx.coroutines.runBlocking
import me.tylerbwong.stack.BaseTest
import me.tylerbwong.stack.api.model.User
import me.tylerbwong.stack.api.service.AuthService
import me.tylerbwong.stack.api.service.UserService
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutError
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutSuccess
import me.tylerbwong.stack.data.auth.LoginResult.LoginError
import me.tylerbwong.stack.data.auth.LoginResult.LoginSuccess
import me.tylerbwong.stack.data.persistence.dao.AnswerDao
import me.tylerbwong.stack.data.persistence.dao.AnswerDraftDao
import me.tylerbwong.stack.data.persistence.dao.QuestionDao
import me.tylerbwong.stack.data.persistence.dao.SearchDao
import me.tylerbwong.stack.data.persistence.dao.UserDao
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import me.tylerbwong.stack.api.model.Response as StackResponse

class AuthRepositoryTest : BaseTest() {

    @Mock
    private lateinit var answerDraftDao: AnswerDraftDao

    @Mock
    private lateinit var questionDao: QuestionDao

    @Mock
    private lateinit var answerDao: AnswerDao

    @Mock
    private lateinit var searchDao: SearchDao

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var authService: AuthService

    private lateinit var authStore: AuthStore
    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        authStore = AuthStore(testSharedPreferences)
        repository = AuthRepository(
            answerDraftDao,
            questionDao,
            answerDao,
            userDao,
            searchDao,
            userService,
            authService,
            authStore
        )
    }

    @Test
    fun `logIn with valid user returns success state`() {
        runBlocking {
            val validUri = Uri.parse("stack://tylerbwong.me/auth/redirect#access_token=1234567")
            whenever(userService.getCurrentUser(any(), any())).thenReturn(
                StackResponse(listOf(testUser), false)
            )
            val result = repository.logIn(validUri)
            assertTrue(result is LoginSuccess)
            verify(userService, times(1)).getCurrentUser(any(), any())
            assertEquals(true, authStore.isAuthenticatedLiveData.value)
            assertNotNull(authStore.accessToken)
        }
    }

    @Test
    fun `logIn with invalid user returns error state`() {
        runBlocking {
            val invalidUri = Uri.parse("stack://tylerbwong.me/auth/redirect#access_token=1234567")
            whenever(userService.getCurrentUser(any(), any())).thenReturn(
                StackResponse(emptyList(), false)
            )
            val result = repository.logIn(invalidUri)
            assertTrue(result is LoginError)
            verify(userService, times(1)).getCurrentUser(any(), any())
            assertEquals(false, authStore.isAuthenticatedLiveData.value)
            assertNull(authStore.accessToken)
        }
    }

    @Test
    fun `logIn with invalid uri returns error state`() {
        runBlocking {
            val invalidUri = Uri.parse("stack://tylerbwong.me/auth?access_token=1234567")
            whenever(userService.getCurrentUser(any(), any())).thenReturn(
                StackResponse(emptyList(), false)
            )
            val result = repository.logIn(invalidUri)
            assertTrue(result is LoginError)
            verify(userService, never()).getCurrentUser(any(), any())
            assertEquals(false, authStore.isAuthenticatedLiveData.value)
            assertNull(authStore.accessToken)
        }
    }

    @Test
    fun `logOut with existing access token returns success state`() {
        runBlocking {
            authStore.setAccessToken(testUri)
            val result = repository.logOut()
            assertTrue(result is LogOutSuccess)
            verify(authService, times(1)).logOut("test")
            verify(answerDraftDao, times(1)).clearDrafts()
            verify(searchDao, times(1)).clearSearches()
            assertTrue(authStore.accessToken.isNullOrBlank())
        }
    }

    @Test
    fun `logOut with absent access token returns error state`() {
        runBlocking {
            authStore.clear()
            val result = repository.logOut()
            assertTrue(result is LogOutError)
            verify(authService, never()).logOut(any(), any())
            verify(answerDraftDao, never()).clearDrafts()
            verify(searchDao, never()).clearSearches()
        }
    }

    @Test
    fun `logOut that comes back with error returns error state`() {
        runBlocking {
            authStore.setAccessToken(testUri)
            whenever(authService.logOut(any(), any()))
                .thenThrow(
                    HttpException(
                        Response.error<ResponseBody>(
                            403,
                            """
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
            whenever(userService.getCurrentUser(any(), any())).thenReturn(
                StackResponse(listOf(testUser), false)
            )
            authStore.setAccessToken(testUri)
            assertEquals(testUser, repository.getCurrentUser())
            verify(userService, times(1)).getCurrentUser(any(), any())
        }
    }

    @Test
    fun `getCurrentUser with absent access token never makes service and db calls and returns null`() {
        runBlocking {
            authStore.clear()
            assertNull(repository.getCurrentUser())
            verify(userService, never()).getCurrentUser(any(), any())
            verify(userDao, never()).insert(any())
        }
    }

    @Test
    fun `getCurrentUser with throwing service call returns null`() {
        runBlocking {
            whenever(userService.getCurrentUser(any(), any()))
                .thenThrow(
                    HttpException(
                        Response.error<ResponseBody>(
                            403,
                            """
                                {
                                    "errorCode": 403
                                }
                            """.trimIndent().toResponseBody("application/json".toMediaTypeOrNull())
                        )
                    )
                )
            authStore.setAccessToken(testUri)
            assertNull(repository.getCurrentUser())
            verify(userService, times(1)).getCurrentUser(any(), any())
            verify(userDao, never()).insert(any())
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
