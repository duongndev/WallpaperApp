package com.duongnd.wallpaper.presentation.favorite.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.duongnd.wallpaper.base.viewmodel.BaseViewModel
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.domain.usecase.DeleteImageDbUseCase
import com.duongnd.wallpaper.domain.usecase.GetImageDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteUseCase: GetImageDbUseCase,
    private val deleteImageDbUseCase: DeleteImageDbUseCase,
) : BaseViewModel() {
    private val listFavoriteFlow = MutableStateFlow<List<ImageEntity>>(emptyList())
    val listFavorite = listFavoriteFlow.asStateFlow()

    fun getListDB() {
        viewModelScope.launch {
            getFavoriteUseCase.execute(GetImageDbUseCase.Param()).collect {
                listFavoriteFlow.value = it
                Log.d("TAG", "getListDB: ${it.size}")
            }
        }
    }

    fun removeFav(image: ImageEntity) {
        viewModelScope.launch {
            deleteImageDbUseCase.execute(DeleteImageDbUseCase.Param(image.id))
        }
    }
}