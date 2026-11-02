package com.duongnd.wallpaper.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.duongnd.wallpaper.domain.model.WallpaperTarget

interface PreviewRepository {
    suspend fun setWallpaper(imgRes: String, target: WallpaperTarget)
    suspend fun saveImage(imageRes: String)
    suspend fun shareImage(imageRes: String): Uri
    suspend fun shareImage(bitmap: Bitmap): Uri
}