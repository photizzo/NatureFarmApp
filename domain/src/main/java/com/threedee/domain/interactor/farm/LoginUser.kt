package com.threedee.domain.interactor.farm

import com.threedee.domain.executor.PostExecutionThread
import com.threedee.domain.executor.ThreadExecutor
import com.threedee.domain.repository.FarmRepository
import com.threedee.domain.usecase.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

/**
 * Use case for log in user
 */
class LoginUser @Inject constructor(
    val farmRepository: FarmRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : CompletableUseCase<LoginUser.Params>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: LoginUser.Params): Completable {
        return farmRepository.loginUser(params)
    }
    data class Params(
        val email: String,
        val password: String
    )
}