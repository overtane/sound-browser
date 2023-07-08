package com.example.soundbrowser.main

import com.example.soundbrowser.freesound.FreeSoundHttpClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SoundRepository {

    private val counts = Channel<Int>()
    fun observeCount() = counts.receiveAsFlow()

    fun soundPagingSource(query: String) : SoundPagingSource {
        return SoundPagingSource(FreeSoundHttpClient, query, counts)
    }
}