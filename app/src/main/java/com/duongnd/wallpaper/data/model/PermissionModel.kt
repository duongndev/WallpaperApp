package com.duongnd.wallpaper.data.model

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.duongnd.wallpaper.utils.Permission

@Keep
data class PermissionModel(
    @param:StringRes
    val title: Int,
    val permissions: List<Permission>,
    var isAllowed: Boolean = false,
)