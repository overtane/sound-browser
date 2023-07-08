package com.example.soundbrowser.sounddb

import kotlinx.serialization.Serializable

@Serializable
data class SoundDbResponse(
    val count: Int,
    val previous: String?,
    val next: String?,
    val results: List<SoundDbResult>,
)

