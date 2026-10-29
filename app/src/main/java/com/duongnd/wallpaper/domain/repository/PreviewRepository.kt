package com.duongnd.wallpaper.domain.repository

import android.graphics.Bitmap
import android.net.Uri
import com.duongnd.wallpaper.domain.model.WallpaperTarget

interface PreviewRepository {
    suspend fun setWallpaper(imgRes: Int, target: WallpaperTarget)
    suspend fun saveImage(imageRes: Int)
    suspend fun shareImage(imageRes: Int): Uri
    suspend fun shareImage(bitmap: Bitmap): Uri
}