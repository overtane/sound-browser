package com.example.soundbrowser.details

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.soundbrowser.main.SoundRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SoundDetailsViewModel(id: Int) : ViewModel() {

    private val _details: MutableStateFlow<SoundDetailsUi?> = MutableStateFlow(null)
    val details: StateFlow<SoundDetailsUi?> = _details.asStateFlow()

    enum class PlayState { LOADING, STOPPED, PLAYING, COMPLETED, ERROR }

    private val _state: MutableStateFlow<PlayState> = MutableStateFlow(PlayState.LOADING)
    val state: StateFlow<PlayState> = _state.asStateFlow()

    private val mediaPlayer = MediaPlayer().apply {
        setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    init {
        viewModelScope.launch {
            SoundRepository.getSound(id)
                .onSuccess { sound ->
                    val uiModel = SoundDetailsUi(sound)
                    mediaPlayer.apply {
                        setOnPreparedListener { _state.update { PlayState.PLAYING } }
                        setOnCompletionListener { _state.update { PlayState.COMPLETED } }
                        setDataSource(uiModel.previewUrl)
                        prepareAsync()
                    }
                    _details.update { uiModel }
                }
                .onFailure {
                    _state.update { PlayState.ERROR }
                }
        }
    }

    fun onButtonClick() = when (state.value) {
        PlayState.STOPPED -> _state.update { PlayState.PLAYING }
        PlayState.PLAYING -> _state.update { PlayState.STOPPED }
        PlayState.ERROR,
        PlayState.LOADING,
        PlayState.COMPLETED -> Unit
    }

    fun playByState(state: PlayState) = when (state) {
        PlayState.STOPPED -> if (mediaPlayer.isPlaying) mediaPlayer.pause() else Unit
        PlayState.PLAYING -> if (mediaPlayer.isPlaying) Unit else mediaPlayer.start()
        PlayState.COMPLETED -> {
            mediaPlayer.seekTo(0)
            _state.update { PlayState.STOPPED }
        }
        PlayState.ERROR,
        PlayState.LOADING -> Unit
    }

    fun onDestroy() {
        mediaPlayer.release()
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