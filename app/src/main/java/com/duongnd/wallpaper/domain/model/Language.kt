package com.duongnd.wallpaper.domain.model

data class Language (
    val languageCode: String,
    val nameRes: Int,
    var selected: Boolean = false
)