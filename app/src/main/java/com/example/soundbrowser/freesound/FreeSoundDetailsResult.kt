package com.example.soundbrowser.freesound


import kotlinx.serialization.Serializable

@Serializable
data class FreeSoundDetailsResult(
    val id: Int,
    val name: String,
    val duration: Double,
    val samplerate: Double,
    val channels: Int,
    val bitdepth: Int,
    val username: String,
    val license: String,
    val previews: FreeSoundPreviews
)