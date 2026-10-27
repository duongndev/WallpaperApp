package com.duongnd.wallpaper.component.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duongnd.wallpaper.base.viewmodel.BaseViewModel
import com.duongnd.wallpaper.data.model.ImageEntity
import com.duongnd.wallpaper.data.usecase.GetListImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val getListImageUseCase: GetListImageUseCase
): BaseViewModel() {
    private val listImageUseCase = MutableStateFlow<List<ImageEntity>>(emptyList())
    var listImage = listImageUseCase.asStateFlow()

    fun getListImage() {
        viewModelScope.launch {
            listImageUseCase.value = getListImageUseCase.execute(GetListImageUseCase.Param())
        }
    }


}