package com.duongnd.wallpaper.domain.usecase

import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.data.repositories.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetRandomImageUseCase @Inject constructor(private val localRepository: LocalRepository):
    UseCase<GetRandomImageUseCase.Param, ImageEntity>() {
    override suspend fun execute(param: Param): ImageEntity {
        return localRepository.getRandomFavorite()!!
    }

    open class Param : UseCase.Param()
}