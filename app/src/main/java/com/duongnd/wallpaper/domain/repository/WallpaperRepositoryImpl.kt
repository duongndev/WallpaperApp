package com.duongnd.wallpaper.domain.repository

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import com.duongnd.wallpaper.domain.model.WallpaperTarget
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WallpaperRepository {

    override suspend fun setWallpaper(
        imgRes: Int,
        target: WallpaperTarget
    ): Unit = withContext(Dispatchers.IO) {

        val bitmap = requireNotNull(
            BitmapFactory.decodeResource(
                context.resources,
                imgRes
            )
        ) {
            "Không thể đọc ảnh từ resource"
        }

        try {
            val wallpaperManager =
                WallpaperManager.getInstance(context)

            val flag = when (target) {
                WallpaperTarget.HOME ->
                    WallpaperManager.FLAG_SYSTEM

                WallpaperTarget.LOCK ->
                    WallpaperManager.FLAG_LOCK

                WallpaperTarget.BOTH ->
                    WallpaperManager.FLAG_SYSTEM or
                            WallpaperManager.FLAG_LOCK
            }

            wallpaperManager.setBitmap(
                bitmap,
                null,
                true,
                flag
            )
        } finally {
            bitmap.recycle()
        }
    }
}