package com.duongnd.wallpaper.domain.usecase

import com.duongnd.wallpaper.domain.repository.PreviewRepository
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val previewRepository: PreviewRepository
) : UseCase<SaveImageUseCase.Param, Unit>() {

    data class Param(val imageRes: Int) : UseCase.Param()

    override suspend fun execute(param: Param) {
        previewRepository.saveImage(param.imageRes)
    }
}
