package org.github.overtane.soundbrowser.details

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import org.github.overtane.soundbrowser.main.SoundRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.github.overtane.soundbrowser.MainActivity

class SoundDetailsViewModel(val id: Int) : ViewModel() {

    private val _details: MutableStateFlow<SoundDetailsUi?> = MutableStateFlow(null)
    val details = _details.asLiveData()

    enum class PlaybackState { LOADING, STOPPED, PLAYING, COMPLETED, ERROR }

    private val _state: MutableStateFlow<PlaybackState> = MutableStateFlow(PlaybackState.LOADING)
    val state = _state.asStateFlow()

    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    fun onPlayButtonClicked() = when (state.value) {
        PlaybackState.STOPPED -> _state.update { PlaybackState.PLAYING }
        PlaybackState.PLAYING -> _state.update { PlaybackState.STOPPED }

        PlaybackState.ERROR,
        PlaybackState.LOADING,
        PlaybackState.COMPLETED -> Unit
    }

    suspend fun playByState(state: PlaybackState) = when (state) {
        PlaybackState.LOADING -> loadMedia(id)
        PlaybackState.STOPPED -> if (mediaPlayer.isPlaying) mediaPlayer.pause() else Unit
        PlaybackState.PLAYING -> if (mediaPlayer.isPlaying) Unit else mediaPlayer.start()
        PlaybackState.COMPLETED -> {
            mediaPlayer.seekTo(0)
            _state.update { PlaybackState.STOPPED }
        }
        PlaybackState.ERROR -> Unit
    }

    fun onUseSoundButtonClicked() {
        /* use setFragmentResult to bundle up fragment result */
    }

    fun onDestroyView() {
        mediaPlayer.reset()
        _state.update { PlaybackState.LOADING }
    }

    fun fragmentResult() = Bundle().apply {
        _details.value?.let {
            this.putString(EXTRA_NAME, it.name)
            this.putString(EXTRA_URL, it.url)
            this.putInt(EXTRA_DURATION, it.duration.toInt())
            this.putInt(EXTRA_CHANNELS, it.channels.toInt())
            this.putInt(EXTRA_SAMPLE_RATE, it.samplerate.toInt())
        }
    }

    private suspend fun loadMedia(id: Int) {
        viewModelScope.launch {
            SoundRepository.getSound(id)
                .onSuccess { sound ->
                    val uiModel = SoundDetailsUi(sound)
                    mediaPlayer.apply {
                        setDataSource(uiModel.previewUrl)
                        setOnPreparedListener { _state.update { PlaybackState.PLAYING } }
                        setOnCompletionListener { _state.update { PlaybackState.COMPLETED } }
                        prepareAsync()
                    }
                    _details.update { uiModel }
                }
                .onFailure {
                    _state.update { PlaybackState.ERROR }
                }
        }
    }

    companion object {
        private const val EXTRA_NAME = "${MainActivity.PACKAGE_NAME}.NAME"
        private const val EXTRA_URL = "${MainActivity.PACKAGE_NAME}.URL"
        private const val EXTRA_DURATION = "${MainActivity.PACKAGE_NAME}.DURATION"
        private const val EXTRA_CHANNELS = "${MainActivity.PACKAGE_NAME}.CHANNELS"
        private const val EXTRA_SAMPLE_RATE = "${MainActivity.PACKAGE_NAME}.SAMPLE_RATE"
    }
}

class SoundDetailsViewModelFactory(
    private val id: Int,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoundDetailsViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return SoundDetailsViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}