package com.example.soundbrowser.main

import com.example.soundbrowser.sounddb.FreeSoundHttpClient

class SoundRepository {

    fun soundPagingSource(query: String) = SoundPagingSource(FreeSoundHttpClient, query)
}