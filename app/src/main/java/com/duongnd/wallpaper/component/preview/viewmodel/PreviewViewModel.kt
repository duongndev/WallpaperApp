package com.duongnd.wallpaper.component.preview.viewmodel

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.viewModelScope
import com.duongnd.wallpaper.base.viewmodel.BaseViewModel
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.domain.model.WallpaperState
import com.duongnd.wallpaper.domain.model.WallpaperTarget
import com.duongnd.wallpaper.domain.usecase.DeleteImageDbUseCase
import com.duongnd.wallpaper.domain.usecase.InsertImageDbUseCase
import com.duongnd.wallpaper.domain.usecase.SetWallpaperUseCase
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val insertImageDbUseCase: InsertImageDbUseCase,
    private val deleteImageDbUseCase: DeleteImageDbUseCase,
    private val setWallpaperUseCase: SetWallpaperUseCase
) : BaseViewModel() {

    private val imageFlow = MutableStateFlow<ImageEntity?>(null)
    val imageF = imageFlow.asStateFlow()

    private val _wallpaperStatus = MutableStateFlow<WallpaperState>(WallpaperState.Idle)
    val wallpaperStatus = _wallpaperStatus.asStateFlow()

    fun setInitialImage(image: ImageEntity) {
        if (imageFlow.value == null) {
            imageFlow.value = image
        }
    }

    fun toggleFavorite(initialImage: ImageEntity) {
        viewModelScope.launch {
            val currentImage = imageFlow.value ?: initialImage
            val newFavoriteState = !currentImage.isFavorite
            val updatedImage = currentImage.copy(isFavorite = newFavoriteState)

            if (newFavoriteState) {
                insertImageDbUseCase.execute(InsertImageDbUseCase.Param(updatedImage)).also {
                    imageFlow.value = updatedImage
                }
            } else {
                deleteImageDbUseCase.execute(DeleteImageDbUseCase.Param(updatedImage.id)).also {
                    imageFlow.value = updatedImage
                }
            }
        }
    }

    fun setWallpaper(target: WallpaperTarget) {
        val image = imageFlow.value ?: return
        viewModelScope.launch {
            _wallpaperStatus.value = WallpaperState.Loading
            runCatching {
               setWallpaperUseCase.execute(SetWallpaperUseCase.Param(image.imgRes, target)).also {
                   _wallpaperStatus.value = WallpaperState.Success
               }
            }.onFailure {
                _wallpaperStatus.value = WallpaperState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun resetWallpaperState(){
        _wallpaperStatus.value = WallpaperState.Idle
    }
}
