package me.tylerbwong.stack.data.auth

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import me.tylerbwong.stack.BaseTest
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutError
import me.tylerbwong.stack.data.auth.LogOutResult.LogOutSuccess
import me.tylerbwong.stack.data.model.User
import me.tylerbwong.stack.data.network.service.StackService
import me.tylerbwong.stack.data.persistence.dao.UserDao
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import retrofit2.HttpException
import retrofit2.Response
import me.tylerbwong.stack.data.model.Response as StackResponse

class AuthRepositoryTest : BaseTest() {

    @Mock
    private lateinit var userDao: UserDao

    @Mock
    private lateinit var stackService: StackService

    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        repository = AuthRepository(userDao, stackService)
    }

    @Test
    fun `logOut with existing access token returns success state`() {
        runBlocking {
            AuthProvider.accessToken = "test"
            val result = repository.logOut()
            assertTrue(result is LogOutSuccess)
            verify(stackService).logOut("test")
            assertTrue(AuthProvider.accessToken.isNullOrBlank())
        }
    }

    @Test
    fun `logOut with absent access token returns error state`() {
        runBlocking {
            AuthProvider.accessToken = null
            val result = repository.logOut()
            assertTrue(result is LogOutError)
            verify(stackService, never()).logOut(any(), any())
        }
    }

    @Test
    fun `logOut that comes back with error returns error state`() {
        runBlocking {
            AuthProvider.accessToken = null
            whenever(stackService.logOut(any(), any()))
                    .thenThrow(HttpException(Response.error<ResponseBody>(403, """
                        {
                            "errorCode": 403
                        }
                    """.trimIndent().toResponseBody("application/json".toMediaTypeOrNull()))))
            val result = repository.logOut()
            assertTrue(result is LogOutError)
            verify(stackService, never()).logOut(any(), any())
        }
    }

    @Test
    fun `getCurrentUser with existing access token makes service and db calls`() {
        runBlocking {
            whenever(stackService.getCurrentUser(any(), any(), any())).thenReturn(
                    StackResponse(listOf(testUser), false)
            )
            AuthProvider.accessToken = "test"
            repository.getCurrentUser()
            verify(stackService).getCurrentUser(any(), any(), any())
            verify(userDao).insert(any())
        }
    }

    @Test
    fun `getCurrentUser with absent access token never makes service and db calls and returns null`() {
        runBlocking {
            AuthProvider.accessToken = null
            assertNull(repository.getCurrentUser())
            verify(stackService, never()).getCurrentUser(any(), any(), any())
            verify(userDao, never()).insert(any())
        }
    }

    @Test
    fun `getCurrentUser with throwing service call returns null`() {
        runBlocking {
            whenever(stackService.getCurrentUser(any(), any(), any()))
                    .thenThrow(HttpException(Response.error<ResponseBody>(403, """
                        {
                            "errorCode": 403
                        }
                    """.trimIndent().toResponseBody("application/json".toMediaTypeOrNull()))))
            AuthProvider.accessToken = "test"
            assertNull(repository.getCurrentUser())
            verify(stackService).getCurrentUser(any(), any(), any())
            verify(userDao, never()).insert(any())
        }
    }

    @Test
    fun `getCurrentUser with throwing db call returns null`() {
        runBlocking {
            whenever(stackService.getCurrentUser(any(), any(), any())).thenReturn(
                    StackResponse(listOf(testUser), false)
            )
            whenever(userDao.insert(any())).thenThrow(IllegalStateException("Could not insert"))
            AuthProvider.accessToken = "test"
            assertNull(repository.getCurrentUser())
            verify(stackService).getCurrentUser(any(), any(), any())
            verify(userDao).insert(any())
        }
    }

    companion object {
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
