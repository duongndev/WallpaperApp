package com.duongnd.wallpaper.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val imgRes: Int,
    var isFavorite: Boolean = false
)
