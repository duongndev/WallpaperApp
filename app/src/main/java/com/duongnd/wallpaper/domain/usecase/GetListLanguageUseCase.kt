package com.duongnd.wallpaper.domain.usecase

import com.duongnd.wallpaper.data.repositories.LocalRepository
import com.duongnd.wallpaper.domain.model.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetListLanguageUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<GetListLanguageUseCase.Param, List<Language>>() {

    open class Param : UseCase.Param()

    override suspend fun execute(param: Param): List<Language> = withContext(Dispatchers.IO) {
        localRepository.getListLanguage()
    }
}