package com.duongnd.wallpaper.data.model

data class Language (
    val languageCode: String,
    val nameRes: Int,
    var selected: Boolean = false
)