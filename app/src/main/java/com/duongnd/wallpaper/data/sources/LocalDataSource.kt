package com.duongnd.wallpaper.data.sources

import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.data.database.ImageDao
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.domain.model.Language
import com.duongnd.wallpaper.domain.model.PermissionModel
import javax.inject.Inject
import com.duongnd.wallpaper.utils.Permission as PermissionRequest

class LocalDataSource @Inject constructor(
    private val imageDao: ImageDao
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

        return listOf(
            ImageEntity(1, title = "Anh 1", R.mipmap.img_01, category = "Nature"),
            ImageEntity(2, title = "Anh 2", R.mipmap.img_02, category = "Nature"),
            ImageEntity(3, title = "Anh 3", R.mipmap.img_03, category = "Animals"),
            ImageEntity(4, title = "Anh 4", R.mipmap.img_04, category = "Abstract"),
            ImageEntity(5, title = "Anh 5", R.mipmap.img_05, category = "Abstract"),
            ImageEntity(6, title = "Anh 6", R.mipmap.img_06, category = "Dark"),
            ImageEntity(7, title = "Anh 7", R.mipmap.img_07, category = "Dark"),
            ImageEntity(8, title = "Anh 8", R.mipmap.img_08, category = "Anime"),
            ImageEntity(9, title = "Anh 9", R.mipmap.img_09, category = "Anime"),
            ImageEntity(10, title = "Anh 10", R.mipmap.img_01, category = "Nature"),
            ImageEntity(11, title = "Anh 11", R.mipmap.img_02, category = "Nature"),
            ImageEntity(12, title = "Anh 12", R.mipmap.img_03, category = "Animals"),
            ImageEntity(13, title = "Anh 13", R.mipmap.img_04, category = "Abstract"),
            ImageEntity(14, title = "Anh 14", R.mipmap.img_05, category = "Abstract"),
            ImageEntity(15, title = "Anh 15", R.mipmap.img_06, category = "Dark"),
            ImageEntity(16, title = "Anh 16", R.mipmap.img_07, category = "Dark"),
            ImageEntity(17, title = "Anh 17", R.mipmap.img_08, category = "Anime"),
            ImageEntity(18, title = "Anh 18", R.mipmap.img_09, category = "Anime"),
        )
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