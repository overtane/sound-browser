package com.example.soundbrowser.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SoundViewModel(
    repository: SoundRepository,
) : ViewModel() {

    private val _count = MutableLiveData<Int>()
    val resultCount: LiveData<String> =
        _count.map { count -> "Found $count sounds matching '$query'" }
    private val counts = repository.observeCount()

    var query: String = DEFAULT_QUERY

    private val validSampleRates = listOf(8000.0, 16000.0, 24000.0, 32000.0, 44100.0, 48000.0)

    // The Pager object calls the load() method from the PagingSource object,
    // providing it with the LoadParams object and receiving the LoadResult object in return.
    val flow = Pager(
        config = PagingConfig(pageSize = 15),
        pagingSourceFactory = { repository.soundPagingSource(query) }
    ).flow
        .cachedIn(viewModelScope)
        .map { pagingData ->
            pagingData
                .filter { item -> item.samplerate in validSampleRates }
                .map { item -> SoundListItem(item) }
        }
        .also { collectCounts() }

    private fun collectCounts() = viewModelScope.launch {
        counts.collect { _count.postValue(it) }
    }

    companion object {
        const val DEFAULT_QUERY = "piano"
    }
}

class SoundViewModelFactory(
    private val soundRepository: SoundRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoundViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return SoundViewModel(soundRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}