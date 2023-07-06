package com.example.soundbrowser.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.soundbrowser.sounddb.FreeSoundHttpClient

class SoundRepository {

    private var source: SoundPagingSource? = null
    val count = MutableLiveData<Int>()

    fun getCount() {
        count.value = source?.count ?: 0
        Log.d("SoundRepository", "Count: ${count.value}")
    }

    fun soundPagingSource(query: String) : SoundPagingSource {
        source = SoundPagingSource(FreeSoundHttpClient, query)
        return source as SoundPagingSource
    }
}