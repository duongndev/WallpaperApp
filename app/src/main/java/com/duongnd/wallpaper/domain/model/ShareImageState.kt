package com.duongnd.wallpaper.domain.model

import android.net.Uri

sealed interface ShareImageState {
    data object Idle : ShareImageState
    data object Loading : ShareImageState
    data class Success(val uri: Uri) : ShareImageState
    data class Error(val message: String) : ShareImageState
}