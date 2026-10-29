package com.duongnd.wallpaper.domain.usecase

import android.net.Uri
import com.duongnd.wallpaper.domain.repository.PreviewRepository
import javax.inject.Inject

class ShareImageUseCase @Inject constructor(
    private val previewRepository: PreviewRepository
) : UseCase<ShareImageUseCase.Param, Uri>() {
    override suspend fun execute(param: Param): Uri {
        return previewRepository.shareImage(param.imgRes)
    }

    data class Param(val imgRes: Int) : UseCase.Param()

}