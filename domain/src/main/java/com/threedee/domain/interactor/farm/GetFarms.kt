package com.threedee.domain.interactor.farm

import com.threedee.domain.executor.PostExecutionThread
import com.threedee.domain.executor.ThreadExecutor
import com.threedee.domain.model.Farm
import com.threedee.domain.repository.FarmRepository
import com.threedee.domain.usecase.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

/**
 * Use case for getting list of [Farm]s from the [FarmRepository]
 */
class GetFarms @Inject constructor(
    val farmRepository: FarmRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : SingleUseCase<List<Farm>, Nothing>(threadExecutor, postExecutionThread) {
    override fun buildUseCaseObservable(params: Nothing?): Single<List<Farm>> {
        return farmRepository.getAllFarms()
    }
}