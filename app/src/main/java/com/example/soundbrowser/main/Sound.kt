package com.example.soundbrowser.main

import com.example.soundbrowser.sounddb.SoundDbResult

data class Sound(
    val id: Int,
    val name: String,
    val type: String,
    val samplerate: String,
    val duration: String,
    val imageUrl: String
) {
    constructor(dbItem: SoundDbResult) : this(
        dbItem.id,
        dbItem.name,
        dbItem.type,
        dbItem.samplerate.toInt().toString(),
        dbItem.duration.toInt().toString(),
        dbItem.images.waveform_m
    ) {}
}
