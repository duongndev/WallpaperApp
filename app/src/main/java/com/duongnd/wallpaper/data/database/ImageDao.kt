package com.duongnd.wallpaper.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duongnd.wallpaper.domain.model.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorites: ImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favorites: List<ImageEntity>)

    @Query("SELECT * FROM images")
    fun getAll(): Flow<List<ImageEntity>>

    @Query("DELETE FROM images")
    suspend fun deleteAll()

    @Query("DELETE FROM images WHERE id = :id")
    suspend fun deleteFavorite(id: Int): Int

}
