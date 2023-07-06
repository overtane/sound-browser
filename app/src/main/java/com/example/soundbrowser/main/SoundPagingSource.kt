package com.example.soundbrowser.main

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.soundbrowser.sounddb.FreeSoundHttpClient
import com.example.soundbrowser.sounddb.SoundDbResult
import kotlinx.coroutines.channels.Channel

class SoundPagingSource(
    private val backend: FreeSoundHttpClient,
    private val query: String,
    private val counts: Channel<Int>
) : PagingSource<Int, SoundDbResult>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, SoundDbResult> {
        return try {
            val page = params.key ?: 1
            val response = backend.get(query, page)
            counts.send(response.count)
            LoadResult.Page(
                data = response.results,
                prevKey = if (response.previous == null) null else page - 1,
                nextKey = if (response.next == null) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SoundDbResult>): Int? {
        val key = state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
        return key
    }

}