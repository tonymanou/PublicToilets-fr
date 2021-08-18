package com.tonymanou.pubpoo.ui.list

import android.os.Bundle
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.savedstate.SavedStateRegistryOwner
import com.tonymanou.pubpoo.model.Toilet
import com.tonymanou.pubpoo.repository.pager.ToiletListRepository
import com.tonymanou.pubpoo.repository.remote.ToiletListApi
import kotlinx.coroutines.flow.Flow

class ToiletListViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    companion object {
        private const val EXTRA_QUERY_ACCESSIBILITY = "com.tonymanou.pubpoo.ui.list.VM:AccessibleOnly"

        fun factory(
            owner: SavedStateRegistryOwner,
            args: Bundle?
        ): ViewModelProvider.Factory = object : AbstractSavedStateViewModelFactory(owner, args) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val viewModel = ToiletListViewModel(handle)

                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                return modelClass.cast(viewModel)
            }
        }
    }

    // TODO use DI here
    private val repository = ToiletListRepository(
        api = ToiletListApi()
    )

    private var currentAccessibleOnly = savedStateHandle.getLiveData(EXTRA_QUERY_ACCESSIBILITY, false)
    private var currentSearchResult: Flow<PagingData<Toilet>>? = null

    val accessibleOnly: LiveData<Boolean>
        get() = currentAccessibleOnly

    fun searchToilets(accessibleOnly: Boolean): Flow<PagingData<Toilet>> {
        val lastResult = currentSearchResult
        if (lastResult != null && currentAccessibleOnly.value == accessibleOnly) {
            return lastResult
        }

        currentAccessibleOnly.value = accessibleOnly
        return repository.getToiletSearchStream(accessibleOnly)
            .cachedIn(viewModelScope)
            .also {
                currentSearchResult = it
            }
    }

}
