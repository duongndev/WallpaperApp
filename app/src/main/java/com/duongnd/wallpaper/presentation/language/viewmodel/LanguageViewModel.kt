package com.duongnd.wallpaper.presentation.language.viewmodel

import androidx.lifecycle.viewModelScope
import com.duongnd.wallpaper.base.viewmodel.BaseViewModel
import com.duongnd.wallpaper.domain.model.Language
import com.duongnd.wallpaper.domain.usecase.GetListLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val getListLanguageUseCase: GetListLanguageUseCase
) : BaseViewModel() {
    private val listLanguageFlow = MutableStateFlow<List<Language>>(emptyList())
    var listLanguage = listLanguageFlow.asStateFlow()

    fun loadListLanguage() {
        viewModelScope.launch {
            listLanguageFlow.value = getListLanguageUseCase.execute(GetListLanguageUseCase.Param())
        }
    }
}