package me.tylerbwong.stack.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import me.tylerbwong.stack.data.persistence.entity.SiteEntity

@Dao
interface SiteDao {

    @Query("SELECT * FROM site")
    fun getSites(): Flow<List<SiteEntity>>

    /**
     * This will get all of the sites the current user is registered on. All of these sites should
     * accept the current access_token as valid.
     *
     * @return a Flow representing the collection of sites a user is registered on. If there is no
     * login, this should emit an empty list
     */
    @Query("SELECT * FROM site WHERE isUserRegistered = 1")
    fun getAssociatedSites(): Flow<List<SiteEntity>>

    @Query("UPDATE site SET isUserRegistered = 0")
    suspend fun clearAssociatedSites()

    @Query("SELECT * FROM SITE WHERE parameter = :parameter")
    suspend fun getSite(parameter: String): SiteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sites: List<SiteEntity>)
}
