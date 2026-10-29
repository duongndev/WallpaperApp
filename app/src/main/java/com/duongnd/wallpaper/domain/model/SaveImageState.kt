package com.duongnd.wallpaper.domain.model

import android.net.Uri

sealed interface SaveImageState {
    data object Idle : SaveImageState
    data object Loading : SaveImageState
    data class Success(val uri: Uri) : SaveImageState
    data class Error(val message: String) : SaveImageState
}