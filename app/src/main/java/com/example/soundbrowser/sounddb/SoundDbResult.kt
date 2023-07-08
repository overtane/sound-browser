package com.example.soundbrowser.sounddb

import kotlinx.serialization.Serializable

@Serializable
data class SoundDbResult(
    val id: Int,
    val name: String,
    val type: String,
    val channels: Int,
    val duration: Double,
    val samplerate: Double,
    val images: SoundDbImageUlrs
)