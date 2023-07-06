package com.example.soundbrowser.main

import com.example.soundbrowser.sounddb.FreeSoundHttpClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SoundRepository {

    private var source: SoundPagingSource? = null
    private val counts = Channel<Int>()
    fun observeCount() = counts.receiveAsFlow()

    fun soundPagingSource(query: String) : SoundPagingSource {
        source = SoundPagingSource(FreeSoundHttpClient, query, counts)
        return source as SoundPagingSource
    }
}