package com.example.soundbrowser.main

import android.util.Log
import com.example.soundbrowser.freesound.FreeSoundDetailsResult
import com.example.soundbrowser.freesound.FreeSoundHttpClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object SoundRepository {

    private val counts = Channel<Int>()
    fun observeCount() = counts.receiveAsFlow()

    fun soundPagingSource(query: String) : SoundPagingSource {
        return SoundPagingSource(FreeSoundHttpClient, query, counts)
    }

    suspend fun getSound(id: Int) = runCatching {
        FreeSoundHttpClient.getSound(id)
    }.onFailure { e ->
        Log.d("SoundRepository", "Exeception: $e")
    }

    }
