package com.example.soundbrowser.main

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.soundbrowser.freesound.FreeSoundHttpClient
import com.example.soundbrowser.freesound.FreeSoundSearchResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class SoundPagingSource(
    private val backend: FreeSoundHttpClient,
    private val query: String,
    private val counts: MutableStateFlow<Int>
) : PagingSource<Int, FreeSoundSearchResult>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, FreeSoundSearchResult> {
        return try {
            val page = params.key ?: 1
            val response = backend.search(query, page)
            counts.update { response.count }
            LoadResult.Page(
                data = response.results,
                prevKey = if (response.previous == null) null else page - 1,
                nextKey = if (response.next == null) null else page + 1
            )
        } catch (e: Exception) {
            Log.d("LOADSTATE", "Load error: $e")
            LoadResult.Error(e)
        }
    }

    // Always start new query from the first page
    override fun getRefreshKey(state: PagingState<Int, FreeSoundSearchResult>): Int? = null
}