package com.duongnd.wallpaper.presentation.preview.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.duongnd.wallpaper.R
import com.duongnd.wallpaper.base.viewmodel.BaseViewModel
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.domain.model.SaveImageState
import com.duongnd.wallpaper.domain.model.ShareImageState
import com.duongnd.wallpaper.domain.model.WallpaperState
import com.duongnd.wallpaper.domain.model.WallpaperTarget
import com.duongnd.wallpaper.domain.usecase.DeleteImageDbUseCase
import com.duongnd.wallpaper.domain.usecase.InsertImageDbUseCase
import com.duongnd.wallpaper.domain.usecase.SaveImageUseCase
import com.duongnd.wallpaper.domain.usecase.SetWallpaperUseCase
import com.duongnd.wallpaper.domain.usecase.ShareImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.duongnd.wallpaper.presentation.widget.WallpaperAppWidget
import com.duongnd.wallpaper.utils.FirebaseAnalyticsUtils

@HiltViewModel
class PreviewViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val insertImageDbUseCase: InsertImageDbUseCase,
    private val deleteImageDbUseCase: DeleteImageDbUseCase,
    private val setWallpaperUseCase: SetWallpaperUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val shareImageUseCase: ShareImageUseCase
) : BaseViewModel() {

    private val imageFlow = MutableStateFlow<ImageEntity?>(null)
    val imageF = imageFlow.asStateFlow()

    private val _wallpaperStatus = MutableStateFlow<WallpaperState>(WallpaperState.Idle)
    val wallpaperStatus = _wallpaperStatus.asStateFlow()

    private val _saveImageState = MutableStateFlow<SaveImageState>(SaveImageState.Idle)
    val saveImageState = _saveImageState.asStateFlow()

    private val _shareImageUri = MutableSharedFlow<ShareImageState>()
    val shareImageUri = _shareImageUri.asSharedFlow()


    fun setInitialImage(image: ImageEntity) {
        if (imageFlow.value == null) {
            imageFlow.value = image
            FirebaseAnalyticsUtils.logViewImageDetail(image.imgRes)
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
                    WallpaperAppWidget.sendRefreshBroadcast(context)
                    FirebaseAnalyticsUtils.logAddFavorite(updatedImage.imgRes)
                }
            } else {
                deleteImageDbUseCase.execute(DeleteImageDbUseCase.Param(updatedImage.id)).also {
                    imageFlow.value = updatedImage
                    WallpaperAppWidget.sendRefreshBroadcast(context)
                    FirebaseAnalyticsUtils.logRemoveFavorite(updatedImage.imgRes)
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
                    val targetStr = when (target) {
                        WallpaperTarget.HOME -> "home"
                        WallpaperTarget.LOCK -> "lock"
                        WallpaperTarget.BOTH -> "both"
                    }
                    FirebaseAnalyticsUtils.logSetWallpaper(targetStr)
                }
            }.onFailure {
                _wallpaperStatus.value = WallpaperState.Error(it.message ?: context.getString(R.string.unknown_error))
                com.duongnd.wallpaper.utils.CrashlyticsUtils.recordException(it)
            }
        }
    }

    fun resetWallpaperState() {
        _wallpaperStatus.value = WallpaperState.Idle
    }

    fun downloadImage() {
        val image = imageFlow.value ?: return
        viewModelScope.launch {
            _saveImageState.value = SaveImageState.Loading
            runCatching {
                saveImageUseCase.execute(SaveImageUseCase.Param(image.imgRes))
            }.onSuccess {
                _saveImageState.value = SaveImageState.Success(android.net.Uri.EMPTY)
                FirebaseAnalyticsUtils.logDownloadImage(image.imgRes)
            }.onFailure {
                _saveImageState.value = SaveImageState.Error(it.message ?: "Unknown error")
                com.duongnd.wallpaper.utils.CrashlyticsUtils.recordException(it)
            }
        }
    }

    fun resetSaveImageState() {
        _saveImageState.value = SaveImageState.Idle
    }


    fun shareImage(image: ImageEntity, bitmap: android.graphics.Bitmap? = null) {
        viewModelScope.launch {
            _shareImageUri.emit(ShareImageState.Loading)
            runCatching {
                shareImageUseCase.execute(ShareImageUseCase.Param(image.imgRes, bitmap))
            }.onSuccess {
                _shareImageUri.emit(ShareImageState.Success(it))
                FirebaseAnalyticsUtils.logShareImage(image.imgRes)
            }.onFailure {
                _shareImageUri.emit(ShareImageState.Error(it.message ?: "Unknown error"))
                com.duongnd.wallpaper.utils.CrashlyticsUtils.recordException(it)
            }
        }
    }

}
