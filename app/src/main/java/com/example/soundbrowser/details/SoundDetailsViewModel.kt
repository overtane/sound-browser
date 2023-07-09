package com.example.soundbrowser.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.soundbrowser.freesound.FreeSoundDetailsResult
import com.example.soundbrowser.freesound.FreeSoundHttpClient
import kotlinx.coroutines.runBlocking

class SoundDetailsViewModel(id: Int): ViewModel() {

    private val _details = MutableLiveData<FreeSoundDetailsResult>()
    val details
        get() = _details

    init {
        // TODO: make async
        runBlocking {
            _details.postValue(FreeSoundHttpClient.get(id))
        }
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