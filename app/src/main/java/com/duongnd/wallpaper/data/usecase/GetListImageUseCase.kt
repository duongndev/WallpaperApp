package com.duongnd.wallpaper.data.usecase

import com.duongnd.wallpaper.data.model.ImageEntity
import com.duongnd.wallpaper.data.repositories.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetListImageUseCase @Inject constructor(private val localRepository: LocalRepository) :
    UseCase<GetListImageUseCase.Param, List<ImageEntity>>() {

    open class Param : UseCase.Param()

    override suspend fun execute(param: GetListImageUseCase.Param): List<ImageEntity> = withContext(Dispatchers.IO) {
        localRepository.getListImage()
    }
}