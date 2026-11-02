package com.duongnd.wallpaper.data.sources

import android.content.Context
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.data.database.ImageDao
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.domain.model.Language
import com.duongnd.wallpaper.domain.model.PermissionModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import com.duongnd.wallpaper.utils.Permission as PermissionRequest

class LocalDataSource @Inject constructor(
    private val imageDao: ImageDao,
    @ApplicationContext private val context: Context
) {

    fun getListLanguage(): List<Language> {
        return listOf(
            Language("en", R.string.english),
            Language("es", R.string.spanish),
            Language("hi", R.string.hindi),
            Language("ko", R.string.korean),
            Language("ja", R.string.japanese),
            Language("de", R.string.german),
            Language("pt", R.string.portuguese),
            Language("fr", R.string.french),
            Language("it", R.string.italian),
            Language("in", R.string.indonesian),
            Language("ru", R.string.russian),
            Language("tr", R.string.turkish),
            Language("zh-TW", R.string.chinese_traditional),
            Language("ar", R.string.arabic),
            Language("vi", R.string.vietnamese),
            Language("th", R.string.thai),
            Language("fil", R.string.filipino),
            Language("ms", R.string.malay),
            Language("pl", R.string.polish),
            Language("bn", R.string.bengali),
            Language("ta", R.string.tamil),
            Language("te", R.string.telugu),
            Language("ro", R.string.romanian),
            Language("cs", R.string.czech),
            Language("el", R.string.greek)
        )
    }

    fun getListImage(): List<ImageEntity> {
        val assetFiles = try {
            context.assets.list("") ?: emptyArray()
        } catch (_: Exception) {
            emptyArray()
        }

        val imageExtensions = listOf(".jpg", ".jpeg", ".png", ".webp")
        val imageFiles = assetFiles.filter { file ->
            imageExtensions.any { ext -> file.lowercase().endsWith(ext) }
        }.sorted()

        if (imageFiles.isEmpty()) {
            return listOf(
                ImageEntity(1, title = "Anh 1", "images/img_01.jpg", category = "Nature"),
                ImageEntity(2, title = "Anh 2", "images/img_02.jpg", category = "Nature"),
                ImageEntity(3, title = "Anh 3", "images/img_03.jpg", category = "Animals"),
                ImageEntity(4, title = "Anh 4", "images/img_04.jpg", category = "Abstract"),
                ImageEntity(5, title = "Anh 5", "images/img_05.jpg", category = "Abstract"),
                ImageEntity(6, title = "Anh 6", "images/img_06.jpg", category = "Dark"),
                ImageEntity(7, title = "Anh 7", "images/img_07.jpg", category = "Dark"),
                ImageEntity(8, title = "Anh 8", "images/img_08.jpg", category = "Anime"),
                ImageEntity(9, title = "Anh 9", "images/img_09.jpg", category = "Anime"),
                ImageEntity(10, title = "Anh 10", "images/img_01.jpg", category = "Nature"),
                ImageEntity(11, title = "Anh 11", "images/img_02.jpg", category = "Nature"),
                ImageEntity(12, title = "Anh 12", "images/img_03.jpg", category = "Animals"),
                ImageEntity(13, title = "Anh 13", "images/img_04.jpg", category = "Abstract"),
                ImageEntity(14, title = "Anh 14", "images/img_05.jpg", category = "Abstract"),
                ImageEntity(15, title = "Anh 15", "images/img_06.jpg", category = "Dark"),
                ImageEntity(16, title = "Anh 16", "images/img_07.jpg", category = "Dark"),
                ImageEntity(17, title = "Anh 17", "images/img_08.jpg", category = "Anime"),
                ImageEntity(18, title = "Anh 18", "images/img_09.jpg", category = "Anime"),
            )
        }

        val categories = listOf("Nature", "Animals", "Abstract", "Dark", "Anime")
        val list = mutableListOf<ImageEntity>()
        var id = 1

        val sourceFiles = if (imageFiles.size < 18) {
            val repeated = mutableListOf<String>()
            while (repeated.size < 18) {
                repeated.addAll(imageFiles)
            }
            repeated.take(18)
        } else {
            imageFiles
        }

        sourceFiles.forEachIndexed { index, fileName ->
            val category = categories[index % categories.size]
            list.add(
                ImageEntity(
                    id = id,
                    title = "Anh $id",
                    imgRes = fileName,
                    category = category
                )
            )
            id++
        }
        return list
    }

    fun getListPermission(): List<PermissionModel> =
        buildList {
            add(PermissionModel(R.string.camera, listOf(PermissionRequest.CAMERA)))
            add(
                PermissionModel(
                    R.string.write_external_storage,
                    listOf(PermissionRequest.WRITE_EXTERNAL_STORAGE)
                )
            )
        }
}
