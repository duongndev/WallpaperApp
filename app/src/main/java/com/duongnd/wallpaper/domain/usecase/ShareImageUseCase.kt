package com.duongnd.wallpaper.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import com.duongnd.wallpaper.domain.repository.PreviewRepository
import javax.inject.Inject

class ShareImageUseCase @Inject constructor(
    private val previewRepository: PreviewRepository
) : UseCase<ShareImageUseCase.Param, Uri>() {
    override suspend fun execute(param: Param): Uri {
        return param.bitmap?.let { 
            previewRepository.shareImage(it)
        } ?: previewRepository.shareImage(param.imgRes)
    }

    data class Param(val imgRes: Int = 0, val bitmap: Bitmap? = null) : UseCase.Param()

}