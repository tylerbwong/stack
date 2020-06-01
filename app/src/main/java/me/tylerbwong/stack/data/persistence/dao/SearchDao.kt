package me.tylerbwong.stack.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import me.tylerbwong.stack.data.persistence.entity.SearchEntity

@Dao
interface SearchDao {

    @Query("SELECT * FROM search WHERE site = :site ORDER BY timestamp desc")
    suspend fun getSearches(site: String): List<SearchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchEntity: SearchEntity)

    @Query("DELETE FROM search")
    suspend fun clearSearches()
}
