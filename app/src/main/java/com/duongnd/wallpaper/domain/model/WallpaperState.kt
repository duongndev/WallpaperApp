package com.duongnd.wallpaper.domain.model

sealed interface WallpaperState {
    data object Idle : WallpaperState
    data object Loading : WallpaperState
    data object Success : WallpaperState
    data class Error(val message: String) : WallpaperState
}