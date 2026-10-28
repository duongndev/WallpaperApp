package com.duongnd.wallpaper.domain.usecase

import com.duongnd.wallpaper.domain.model.ImageEntity
import com.duongnd.wallpaper.data.repositories.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetImageDbUseCase @Inject constructor(private val localRepository: LocalRepository):
    UseCase<GetImageDbUseCase.Param, Flow<List<ImageEntity>>>() {
    override suspend fun execute(param: Param): Flow<List<ImageEntity>> = flow {
         localRepository.getList().collect {
             emit(it)
         }
    }

    open class Param : UseCase.Param()
}