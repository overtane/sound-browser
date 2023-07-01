package com.example.soundbrowser.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.soundbrowser.sounddb.FreeSoundHttpClient
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SoundViewModel : ViewModel() {

    private val sounddb = FreeSoundHttpClient

    init {
        runBlocking { launch { greet() } }
    }

    suspend fun greet() = coroutineScope {
        val text = try {
            sounddb.search("piano")
        } catch (e: Exception) {
            e.localizedMessage ?: "error"
        }
        Log.d("SOUND SEARCH", " $text")
    }

}