package com.duongnd.wallpaper.domain.repository

import com.duongnd.wallpaper.domain.model.WallpaperTarget

interface WallpaperRepository {
    suspend fun setWallpaper(imgRes: Int, target: WallpaperTarget)
}