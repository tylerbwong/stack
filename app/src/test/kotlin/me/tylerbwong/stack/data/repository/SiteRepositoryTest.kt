package me.tylerbwong.stack.data.repository

import kotlinx.coroutines.runBlocking
import me.tylerbwong.stack.BaseTest
import me.tylerbwong.stack.api.model.Response
import me.tylerbwong.stack.api.model.Site
import me.tylerbwong.stack.api.service.SiteService
import me.tylerbwong.stack.data.persistence.dao.SiteDao
import me.tylerbwong.stack.data.site.SiteStore
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SiteRepositoryTest : BaseTest() {

    @Mock
    private lateinit var siteDao: SiteDao

    @Mock
    private lateinit var siteService: SiteService

    @Mock
    private lateinit var siteStore: SiteStore

    private lateinit var repository: SiteRepository

    @Before
    fun setUp() {
        repository = SiteRepository(siteDao, siteService, siteStore)
    }

    @Test
    fun `null current site fetches site from service`() {
        runBlocking {
            whenever(siteDao.getSite(any())).thenReturn(null)
            whenever(siteService.getSites()).thenReturn(testResponse)
            repository.fetchSitesIfNecessary()
            verify(siteService, times(1)).getSites()
            verify(siteDao, times(1)).insert(any())
        }
    }

    @Test
    fun `one error from service retries once`() {
        runBlocking {
            whenever(siteDao.getSite(any())).thenReturn(null)
            whenever(siteService.getSites())
                .thenThrow(RuntimeException("Request error"))
                .thenReturn(testResponse)
            repository.fetchSitesIfNecessary()
            verify(siteService, times(2)).getSites()
            verify(siteDao, times(1)).insert(any())
        }
    }

    @Test
    fun `two errors from service retries twice`() {
        runBlocking {
            whenever(siteDao.getSite(any())).thenReturn(null)
            whenever(siteService.getSites())
                .thenThrow(RuntimeException("Request error"))
                .thenThrow(RuntimeException("Request error"))
                .thenReturn(testResponse)
            repository.fetchSitesIfNecessary()
            verify(siteService, times(3)).getSites()
            verify(siteDao, times(1)).insert(any())
        }
    }

    @Test
    fun `three errors from service retries three times`() {
        runBlocking {
            whenever(siteDao.getSite(any())).thenReturn(null)
            whenever(siteService.getSites())
                .thenThrow(RuntimeException("Request error"))
                .thenThrow(RuntimeException("Request error"))
                .thenThrow(RuntimeException("Request error"))
                .thenReturn(testResponse)
            repository.fetchSitesIfNecessary()
            verify(siteService, times(4)).getSites()
            verify(siteDao, times(1)).insert(any())
        }
    }

    @Test(expected = Exception::class)
    fun `four errors from service throws an error`() {
        runBlocking {
            whenever(siteDao.getSite(any())).thenReturn(null)
            whenever(siteService.getSites())
                .thenThrow(RuntimeException("Request error"))
                .thenThrow(RuntimeException("Request error"))
                .thenThrow(RuntimeException("Request error"))
                .thenThrow(RuntimeException("Request error"))
                .thenReturn(testResponse)
            repository.fetchSitesIfNecessary()
            verify(siteService, times(5)).getSites()
            verify(siteDao, never()).insert(any())
        }
    }

    companion object {
        private val testResponse = Response(
            items = listOf(
                Site(
                    "Stack Overflow",
                    "stackoverflow",
                    "stackoverflow.com",
                    "Everyone",
                    "https://cdn.sstatic.net/Sites/stackoverflow/Img/apple-touch-icon.png"
                )
            )
        )
    }
}
