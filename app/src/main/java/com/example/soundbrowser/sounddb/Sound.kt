package com.example.soundbrowser.sounddb

data class Sound(
    val id: Int,
    val name: String,
    val type: String,
    val samplerate: Int,
    val imageUrl: String
)
