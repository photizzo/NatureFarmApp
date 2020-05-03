package com.threedee.domain.interactor.farm

import com.threedee.domain.executor.PostExecutionThread
import com.threedee.domain.executor.ThreadExecutor
import com.threedee.domain.model.Farm
import com.threedee.domain.repository.FarmRepository
import com.threedee.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

/**
 * Use case for deleting a single [Farm] from the [FarmRepository]
 */
class DeleteFarm @Inject constructor(
    val farmRepository: FarmRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : CompletableUseCase<Farm>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: Farm): Completable {
        return farmRepository.deleteFarm(params)
    }
}