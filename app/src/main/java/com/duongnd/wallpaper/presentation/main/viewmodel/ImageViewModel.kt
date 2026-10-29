package com.duongnd.wallpaper.presentation.main.viewmodel

import androidx.lifecycle.viewModelScope
import com.duongnd.wallpaper.base.viewmodel.BaseViewModel
import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.domain.usecase.DeleteImageDbUseCase
import com.duongnd.wallpaper.domain.usecase.GetImageDbUseCase
import com.duongnd.wallpaper.domain.usecase.GetListImageUseCase
import com.duongnd.wallpaper.domain.usecase.InsertImageDbUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val getListImageUseCase: GetListImageUseCase,
    private val insertImageDbUseCase: InsertImageDbUseCase,
    private val deleteImageDbUseCase: DeleteImageDbUseCase,
    private val getImageDbUseCase: GetImageDbUseCase
) : BaseViewModel() {
    private val listImageUseCase = MutableStateFlow<List<ImageEntity>>(emptyList())
    var listImage = listImageUseCase.asStateFlow()

    private var allImages: List<ImageEntity> = emptyList()
    private var currentCategory: String = "All"

    private val listCategory = MutableStateFlow<List<String>>(emptyList())
    var listCategoryState = listCategory.asStateFlow()




    fun getListImage() {
        viewModelScope.launch {
            val localList = getListImageUseCase.execute(GetListImageUseCase.Param())
            getImageDbUseCase.execute(GetImageDbUseCase.Param()).collect { dbList ->
                val dbIds = dbList.map { it.id }.toSet()
                allImages = localList.map {
                    it.copy(isFavorite = dbIds.contains(it.id))
                }
                listCategory.value = listOf("All") + allImages.map { it.category }.distinct()
                filterByCategory(currentCategory)
            }
        }
    }

    fun filterByCategory(category: String) {
        currentCategory = category
        if (category == "All") {
            listImageUseCase.value = allImages
        } else {
            listImageUseCase.value = allImages.filter { it.category == category }
        }
    }

    fun insertImageDb(image: ImageEntity) {
        viewModelScope.launch {
            if (image.isFavorite) {
                insertImageDbUseCase.execute(InsertImageDbUseCase.Param(image))
            } else {
                deleteImageDbUseCase.execute(DeleteImageDbUseCase.Param(image.id))
            }
        }
    }

    fun searchImage(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                filterByCategory(currentCategory)
            } else {
                listImageUseCase.value = allImages.filter {
                    it.title.contains(query, ignoreCase = true)
                }
            }
        }
    }


}