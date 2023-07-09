package com.example.soundbrowser.main

import com.example.soundbrowser.freesound.FreeSoundSearchResult

data class SoundListItem(
    val id: Int,
    val name: String,
    val samplerate: String,
    val duration: String,
    val imageUrl: String
) {
    constructor(freeSoundItem: FreeSoundSearchResult) : this(
        id = freeSoundItem.id,
        name = freeSoundItem.name,
        samplerate = freeSoundItem.samplerate.toInt().toString(),
        duration = freeSoundItem.duration.toInt().toString(),
        imageUrl = freeSoundItem.images.waveform_m
    )
}
