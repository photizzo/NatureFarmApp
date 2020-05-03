package com.threedee.presentation.state

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

enum class ResourceState {
    LOADING, SUCCESS, ERROR
}

data class Data<out T> constructor(
    val resourceState: ResourceState,
    val data: T? = null,
    val message: String? = null,
    val throwable: Throwable? = null
)

@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory @Inject constructor(private val viewModels: MutableMap<Class<out ViewModel>,
        Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}