package com.duongnd.wallpaper.domain.usecase

import com.duongnd.wallpaper.data.repositories.LocalRepository
import javax.inject.Inject

class DeleteImageDbUseCase @Inject constructor(
    private val localRepository: LocalRepository
) : UseCase<DeleteImageDbUseCase.Param, Unit>() {
    override suspend fun execute(param: Param) {
        localRepository.deleteFavorite(param.id)
    }

    data class Param(val id: Int) : UseCase.Param()
}