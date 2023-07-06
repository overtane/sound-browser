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
import kotlinx.coroutines.launch

class SoundViewModel(
    private val repository: SoundRepository,
) : ViewModel() {

    private val _count = MutableLiveData<Int>()
    val resultCount: LiveData<String> = _count.map { count -> "$count sounds found"}
    private val counts = repository.observeCount()
    var query: String = DEFAULT_QUERY
    
    // The Pager object calls the load() method from the PagingSource object,
    // providing it with the LoadParams object and receiving the LoadResult object in return.
    val flow = Pager(
        config = PagingConfig(pageSize = 15),
        pagingSourceFactory = { repository.soundPagingSource(query) }
    ).flow
        .cachedIn(viewModelScope)
        .also { collectCounts() }

    private fun collectCounts() = viewModelScope.launch {
        counts.collect { _count.postValue(it) }
    }

    companion object {
        const val DEFAULT_QUERY = "piano"
    }
}

class SoundViewModelFactory(
    private val soundRepository: SoundRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoundViewModel::class.java)) {
            @Suppress("unchecked_cast")
            return SoundViewModel(soundRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}