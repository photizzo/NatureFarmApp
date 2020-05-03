package com.threedee.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.threedee.domain.interactor.farm.AddFarm
import com.threedee.domain.interactor.farm.DeleteFarm
import com.threedee.domain.interactor.farm.GetFarms
import com.threedee.domain.interactor.farm.UpdateFarm
import com.threedee.domain.model.Farm
import com.threedee.presentation.state.Resource
import com.threedee.presentation.state.ResourceState
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import javax.inject.Inject

/**
 * [FarmViewModel] handle all interactions with the UI layer
 */
open class FarmViewModel @Inject constructor(
    private val getFarms: GetFarms,
    private val addFarm: AddFarm,
    private val updateFarm: UpdateFarm,
    private val deleteFarm: DeleteFarm
) : ViewModel() {
    //mutable livedata should be private to a single class
    private val _getFarmsLiveData: MutableLiveData<Resource<List<Farm>>> = MutableLiveData()
    private val _addFarmLiveData: MutableLiveData<Resource<Unit>> = MutableLiveData()
    private val _deleteFarmLiveData: MutableLiveData<Resource<Unit>> = MutableLiveData()
    private val _updateFarmLiveData: MutableLiveData<Resource<Unit>> = MutableLiveData()

    //exposing MutableLivedata to corresponding Livedata objects
    private val getFarmsLiveData: MutableLiveData<Resource<List<Farm>>>
        get() = _getFarmsLiveData
    private val addFarmLiveData: MutableLiveData<Resource<Unit>>
        get() = _addFarmLiveData
    private val deleteFarmLiveData: MutableLiveData<Resource<Unit>>
        get() = _deleteFarmLiveData
    private val updateFarmLiveData: MutableLiveData<Resource<Unit>>
        get() = _updateFarmLiveData

    override fun onCleared() {
        getFarms.dispose()
        super.onCleared()
    }

    /**
     * Get all farms
     */
    fun getFarms() {
        _getFarmsLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        getFarms.execute(GetFarmsSubscriber())
    }

    /**
     * Add a single farm
     */
    fun addFarm(param: Farm){
        _addFarmLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        addFarm.execute(AddFarmCompletableSubscriber(), param)
    }

    /**
     * Delete a single farm
     */
    fun deleteFarm(param: Farm){
        _deleteFarmLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        deleteFarm.execute(DeleteFarmCompletableSubscriber(), param)
    }

    /**
     * Update a single farm
     */
    fun updateFarm(param: Farm){
        _updateFarmLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        updateFarm.execute(UpdateFarmCompletableSubscriber(), param)
    }

    /**
     *
     */
    inner class GetFarmsSubscriber : DisposableSingleObserver<List<Farm>>() {
        override fun onSuccess(data: List<Farm>) {
            _getFarmsLiveData.postValue(
                Resource(
                    ResourceState.SUCCESS,
                    data, null
                )
            )
        }

        override fun onError(e: Throwable) {
            _getFarmsLiveData.postValue(Resource(ResourceState.ERROR, null, e.localizedMessage))
        }
    }

    inner class AddFarmCompletableSubscriber: DisposableCompletableObserver() {
        override fun onComplete() {
            _addFarmLiveData.postValue(Resource(ResourceState.SUCCESS, null, null))
        }

        override fun onError(e: Throwable) {
            _addFarmLiveData.postValue(Resource(ResourceState.ERROR, null, e.localizedMessage))
        }
    }

    inner class DeleteFarmCompletableSubscriber: DisposableCompletableObserver() {
        override fun onComplete() {
            _deleteFarmLiveData.postValue(Resource(ResourceState.SUCCESS, null, null))
        }

        override fun onError(e: Throwable) {
            _deleteFarmLiveData.postValue(Resource(ResourceState.ERROR, null, e.localizedMessage))
        }
    }

    inner class UpdateFarmCompletableSubscriber: DisposableCompletableObserver() {
        override fun onComplete() {
            _updateFarmLiveData.postValue(Resource(ResourceState.SUCCESS, null, null))
        }

        override fun onError(e: Throwable) {
            _updateFarmLiveData.postValue(Resource(ResourceState.ERROR, null, e.localizedMessage))
        }
    }
}