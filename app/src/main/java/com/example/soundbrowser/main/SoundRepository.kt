package com.example.soundbrowser.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.soundbrowser.sounddb.FreeSoundHttpClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking

class SoundRepository {

    private var source: SoundPagingSource? = null
    private val counts = Channel<Int>()
    fun observeCount() = counts.receiveAsFlow()

    fun soundPagingSource(query: String) : SoundPagingSource {
        source = SoundPagingSource(FreeSoundHttpClient, query, counts)
        return source as SoundPagingSource
    }
}