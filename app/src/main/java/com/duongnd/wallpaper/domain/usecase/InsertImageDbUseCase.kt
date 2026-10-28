package com.duongnd.wallpaper.domain.usecase

import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.data.repositories.LocalRepository
import javax.inject.Inject

class InsertImageDbUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : UseCase<InsertImageDbUseCase.Param, Unit>() {
    override suspend fun execute(param: Param) {
        localRepository.insert(param.imageEntity)
    }

    data class Param(val imageEntity: ImageEntity) : UseCase.Param()
}