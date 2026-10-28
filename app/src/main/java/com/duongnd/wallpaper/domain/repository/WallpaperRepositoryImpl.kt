package com.duongnd.wallpaper.domain.repository

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.domain.model.WallpaperTarget
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : WallpaperRepository {

    override suspend fun setWallpaper(
        imgRes: Int,
        target: WallpaperTarget
    ): Unit = withContext(Dispatchers.IO) {
        if (imgRes <= 0) {
            throw IllegalArgumentException(context.getString(R.string.invalid_image_resource))
        }

        val wallpaperManager = WallpaperManager.getInstance(context)

        if (!wallpaperManager.isSetWallpaperAllowed) {
            throw IllegalStateException(context.getString(R.string.setting_wallpaper_is_restricted_on_this_device))
        }

        val bitmap = try {
            BitmapFactory.decodeResource(context.resources, imgRes)
        } catch (_: Exception) {
            null
        } ?: throw IOException(context.getString(R.string.failed_to_decode_image_resource))

        try {
            val flag = when (target) {
                WallpaperTarget.HOME -> WallpaperManager.FLAG_SYSTEM
                WallpaperTarget.LOCK -> WallpaperManager.FLAG_LOCK
                WallpaperTarget.BOTH -> WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK
            }

            wallpaperManager.setBitmap(bitmap, null, true, flag)
        } catch (e: SecurityException) {
            throw SecurityException(
                context.getString(
                    R.string.permission_denied_to_set_wallpaper,
                    e.message
                ))
        } catch (e: IOException) {
            throw IOException(context.getString(R.string.error_saving_wallpaper, e.message))
        } catch (e: Exception) {
            throw Exception(context.getString(R.string.failed_to_set_wallpaper, e.message))
        } finally {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        }
    }
}
