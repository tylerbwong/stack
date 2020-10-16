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

    @Query("SELECT * FROM SITE WHERE parameter = :parameter")
    suspend fun getSite(parameter: String): SiteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sites: List<SiteEntity>)
}
