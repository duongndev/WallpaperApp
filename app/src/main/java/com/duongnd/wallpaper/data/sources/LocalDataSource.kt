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
            ImageEntity(1, R.mipmap.img_01),
            ImageEntity(2, R.mipmap.img_02, ),
            ImageEntity(3, R.mipmap.img_03),
            ImageEntity(4, R.mipmap.img_01),
            ImageEntity(5, R.mipmap.img_02),
            ImageEntity(6, R.mipmap.img_03),
            ImageEntity(7, R.mipmap.img_01),
            ImageEntity(8, R.mipmap.img_02),
            ImageEntity(9, R.mipmap.img_03),
            ImageEntity(10, R.mipmap.img_01),
            ImageEntity(11, R.mipmap.img_02),
            ImageEntity(12, R.mipmap.img_03),
            ImageEntity(13, R.mipmap.img_01),
            ImageEntity(14, R.mipmap.img_02),
            ImageEntity(15, R.mipmap.img_03),
            ImageEntity(16, R.mipmap.img_01),
            ImageEntity(17, R.mipmap.img_02),
            ImageEntity(18, R.mipmap.img_03),
            ImageEntity(19, R.mipmap.img_01),
            ImageEntity(20, R.mipmap.img_02),
            ImageEntity(21, R.mipmap.img_03),
        )
    }







    fun getListPermission(): List<PermissionModel> =
        buildList {
            add(PermissionModel(R.string.camera, listOf(PermissionRequest.CAMERA)))
        }
}