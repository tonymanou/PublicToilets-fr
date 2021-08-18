package com.tonymanou.pubpoo.repository.pager

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tonymanou.pubpoo.contract.ToiletListApiContract
import com.tonymanou.pubpoo.model.Toilet
import kotlinx.coroutines.flow.Flow

class ToiletListRepository(
    private val api: ToiletListApiContract
) {

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    fun getToiletSearchStream(accessibleOnly: Boolean): Flow<PagingData<Toilet>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ToiletPagingSource(api, accessibleOnly) }
        ).flow
    }
}
