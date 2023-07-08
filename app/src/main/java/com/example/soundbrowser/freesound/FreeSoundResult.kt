package com.example.soundbrowser.freesound

import kotlinx.serialization.Serializable

@Serializable
data class FreeSoundResult(
    val id: Int,
    val name: String,
    val duration: Double,
    val samplerate: Double,
    val images: FreeSoundImageUlrs
)