package com.tonymanou.pubpoo.repository.pager

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tonymanou.pubpoo.contract.ToiletListApiContract
import com.tonymanou.pubpoo.model.Toilet

class ToiletPagingSource(
    private val api: ToiletListApiContract,
    private val accessibleOnly: Boolean,
) : PagingSource<Int, Toilet>() {

    companion object {
        private const val TAG = "ToiletPaging"

        private const val DEFAULT_OFFSET = 0
    }

    override fun getRefreshKey(state: PagingState<Int, Toilet>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { closestPosition ->
                closestPosition.prevKey?.plus(1)
                    ?: closestPosition.nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Toilet> {
        val position = params.key ?: DEFAULT_OFFSET
        val limit = params.loadSize
        return try {
            val response = api.fetch(accessibleOnly, position, limit)
            LoadResult.Page(
                data = response,
                prevKey = if (position == DEFAULT_OFFSET) null else position - 1,
                nextKey = if (response.isEmpty()) null else position + limit
            )
        } catch (e: Exception) {
            Log.w(TAG, "Failed to fetch toilets at $position (limit=$limit) ", e)
            LoadResult.Error(e)
        }
    }

}
