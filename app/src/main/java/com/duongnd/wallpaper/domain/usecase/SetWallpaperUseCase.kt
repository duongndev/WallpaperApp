package com.duongnd.wallpaper.domain.usecase

import com.duongnd.wallpaper.domain.model.WallpaperTarget
import com.duongnd.wallpaper.domain.repository.WallpaperRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetWallpaperUseCase @Inject constructor(
    private val wallpaperRepository: WallpaperRepository
) : UseCase<SetWallpaperUseCase.Param, Unit>() {
    override suspend fun execute(param: Param) {
        wallpaperRepository.setWallpaper(imgRes = param.imgRes, target = param.target)
    }

    data class Param(val imgRes: Int, val target: WallpaperTarget) : UseCase.Param()
}