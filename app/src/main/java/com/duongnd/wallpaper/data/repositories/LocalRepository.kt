package com.duongnd.wallpaper.data.repositories

import com.duongnd.wallpaper.data.database.ImageDao
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.domain.model.Language
import com.duongnd.wallpaper.data.sources.LocalDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val imageDao: ImageDao
) {
    fun getListImage(): List<ImageEntity> = localDataSource.getListImage()
    fun getListLanguage(): List<Language> = localDataSource.getListLanguage()

    fun getList(): Flow<List<ImageEntity>> = imageDao.getAll()

    suspend fun insert(favorites: ImageEntity) = imageDao.insert(favorites)
    suspend fun deleteFavorite(id: Int) = imageDao.deleteFavorite(id)

}