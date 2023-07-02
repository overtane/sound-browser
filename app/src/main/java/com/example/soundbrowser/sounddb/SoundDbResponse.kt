package com.example.soundbrowser.sounddb

import kotlinx.serialization.Serializable

@Serializable
data class SoundDbResponse(
    val count: Int,
    val previous: String?,
    val next: String?,
    val results: List<SoundDbResult>
)

@Serializable
data class SoundDbResult(
    val id: Int,
    val name: String,
    val type: String,
    val channels: Int,
    val duration: Double,
    val samplerate: Double
)
