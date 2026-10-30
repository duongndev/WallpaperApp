package com.duongnd.wallpaper.domain.usecase

import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.data.repositories.LocalRepository
import javax.inject.Inject

class GetRandomImageUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<GetRandomImageUseCase.Param, ImageEntity>() {
    override suspend fun execute(param: Param): ImageEntity {
        val favorite = localRepository.getRandomFavorite()
        if (favorite != null) {
            return favorite
        }
        val listImage = localRepository.getListImage()
        return listImage.random()
    }

    open class Param : UseCase.Param()
}