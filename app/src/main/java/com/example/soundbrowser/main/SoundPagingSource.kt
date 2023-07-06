package com.example.soundbrowser.main

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.soundbrowser.sounddb.FreeSoundHttpClient
import com.example.soundbrowser.sounddb.SoundDbResult

class SoundPagingSource(
    private val backend: FreeSoundHttpClient,
    private val query: String
) : PagingSource<Int, SoundDbResult>() {

    var count = 0
    init {
        Log.d("SoundPagingSource" ,"New query: $query")
    }

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, SoundDbResult> {
        return try {
            val page = params.key ?: 1
            val response = backend.get(query, page)
            count = response.count
            Log.d("SoundPagingSource" ,"Page: $page, count: $count")
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