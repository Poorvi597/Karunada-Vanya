package com.karunadavanya.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WildlifeDao {
    @Query("SELECT * FROM wildlife ORDER BY name")
    fun observeAll(): Flow<List<WildlifeEntity>>

    @Query("SELECT * FROM wildlife WHERE id = :id")
    fun observeById(id: String): Flow<WildlifeEntity?>

    @Query(
        """
        SELECT * FROM wildlife
        WHERE name LIKE '%' || :query || '%'
           OR scientificName LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
        ORDER BY name
        """
    )
    fun search(query: String): Flow<List<WildlifeEntity>>

    @Query("SELECT * FROM wildlife WHERE category = :category ORDER BY name")
    fun observeByCategory(category: String): Flow<List<WildlifeEntity>>

    @Query("SELECT COUNT(*) FROM wildlife")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<WildlifeEntity>)
}

@Dao
interface AlertDao {
    @Query("SELECT * FROM alerts ORDER BY reportedAt DESC")
    fun observeAll(): Flow<List<AlertEntity>>

    @Query("SELECT * FROM alerts WHERE id = :id")
    fun observeById(id: String): Flow<AlertEntity?>

    @Query("SELECT COUNT(*) FROM alerts")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<AlertEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AlertEntity)
}

@Dao
interface SightingReportDao {
    @Query("SELECT * FROM sighting_reports ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<SightingReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(report: SightingReportEntity)
}
