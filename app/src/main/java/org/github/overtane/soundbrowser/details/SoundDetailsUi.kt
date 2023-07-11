package org.github.overtane.soundbrowser.details

import org.github.overtane.soundbrowser.freesound.FreeSoundDetailsResult

data class SoundDetailsUi(
    val id: Int,
    val name: String,
    val duration: String,
    val samplerate: String,
    val channels: String,
    val bitdepth: String,
    val username: String,
    val previewUrl: String
) {
    constructor(freeSoundResult: FreeSoundDetailsResult) : this(
        id = freeSoundResult.id,
        name = freeSoundResult.name,
        duration = freeSoundResult.duration.toInt().toString() + " s",
        samplerate = freeSoundResult.samplerate.toInt().toString() + " Hz",
        channels = freeSoundResult.channels.toString(),
        bitdepth = freeSoundResult.bitdepth.toString(),
        username = freeSoundResult.username,
        previewUrl = freeSoundResult.previews.preview_lq_mp3
    )
}
