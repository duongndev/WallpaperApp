package com.duongnd.wallpaper.data.sources

import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.utils.Constant
import com.duongnd.wallpaper.data.model.Language
import com.duongnd.wallpaper.data.model.PermissionModel
import javax.inject.Inject
import com.duongnd.wallpaper.utils.Permission as PermissionRequest

class LocalDataSource @Inject constructor() {

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


    fun getListPermission(): List<PermissionModel> =
        buildList {
            add(PermissionModel(R.string.camera, listOf(PermissionRequest.CAMERA)))
        }
}