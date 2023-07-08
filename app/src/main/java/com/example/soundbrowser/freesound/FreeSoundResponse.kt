package com.example.soundbrowser.freesound

import kotlinx.serialization.Serializable

@Serializable
data class FreeSoundResponse(
    val count: Int,
    val previous: String?,
    val next: String?,
    val results: List<FreeSoundResult>,
)

