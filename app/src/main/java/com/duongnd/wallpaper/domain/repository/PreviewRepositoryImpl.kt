package com.duongnd.wallpaper.domain.repository

import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.domain.model.WallpaperTarget
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class PreviewRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreviewRepository {

    override suspend fun setWallpaper(
        imgRes: Int,
        target: WallpaperTarget
    ): Unit = withContext(Dispatchers.IO) {

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
                )
            )
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

    override suspend fun saveImage(imageRes: Int) = withContext(Dispatchers.IO) {
        val resolver = context.contentResolver
        var imageUri: Uri? = null

        val bitmap = try {
            BitmapFactory.decodeResource(context.resources, imageRes)
        } catch (_: Exception) {
            null
        } ?: throw IOException(context.getString(R.string.failed_to_decode_image_resource))

        try {
            val contentValues = ContentValues().apply {
                put(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    "wallpaper_${System.currentTimeMillis()}.jpg"
                )
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
            }

            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            imageUri?.let { uri ->
                resolver.openOutputStream(uri)?.use { outputStream ->
                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                        throw IOException(context.getString(R.string.failed_to_save_image))
                    }
                }
            } ?: throw IOException(context.getString(R.string.failed_to_save_image))
        } finally {
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        }
    }

    override suspend fun shareImage(bitmap: Bitmap): Uri = withContext(Dispatchers.IO) {
        var imgFile: File? = null

        try {
            val directory = File(
                context.cacheDir,
                "shared_images"
            )

            if (!directory.exists() && !directory.mkdirs()) {
                throw IOException(context.getString(R.string.can_create_photo_folder))
            }

            imgFile = File(
                directory,
                "wallpaper_${System.currentTimeMillis()}.png"
            )

            FileOutputStream(imgFile).use { output ->
                if (!bitmap.compress(
                        Bitmap.CompressFormat.PNG,
                        100,
                        output
                    )
                ) {
                    throw IOException(context.getString(R.string.can__save_the_image))
                }
            }

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                imgFile
            )
        } catch (e: Exception) {
            imgFile?.delete()
            throw e
        }
    }

    override suspend fun shareImage(imageRes: Int): Uri = withContext(Dispatchers.IO) {
        val bitmap = BitmapFactory.decodeResource(
            context.resources,
            imageRes
        ) ?: throw IOException(context.getString(R.string.failed_to_decode_image_resource))
        
        try {
            shareImage(bitmap)
        } finally {
            bitmap.recycle()
        }
    }
}
