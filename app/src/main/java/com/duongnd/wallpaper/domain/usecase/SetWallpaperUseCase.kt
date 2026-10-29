package com.duongnd.wallpaper.domain.usecase

import com.duongnd.wallpaper.domain.model.WallpaperTarget
import com.duongnd.wallpaper.domain.repository.PreviewRepository
import javax.inject.Inject

class SetWallpaperUseCase @Inject constructor(
    private val previewRepository: PreviewRepository
) : UseCase<SetWallpaperUseCase.Param, Unit>() {
    override suspend fun execute(param: Param) {
        previewRepository.setWallpaper(imgRes = param.imgRes, target = param.target)
    }

    data class Param(val imgRes: Int, val target: WallpaperTarget) : UseCase.Param()
}