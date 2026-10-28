package com.duongnd.wallpaper.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duongnd.wallpaper.domain.model.ImageEntity

@Database(
    entities = [ImageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imagesDao(): ImageDao
}
