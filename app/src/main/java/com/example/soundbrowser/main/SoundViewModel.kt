package com.example.soundbrowser.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.launch

class SoundViewModel(
    private val repository: SoundRepository,
) : ViewModel() {

    private val _resultCount = MutableLiveData<String>()
    val resultCount: LiveData<String>
        get() = _resultCount

    var query: String = DEFAULT_QUERY

    // The Pager object calls the load() method from the PagingSource object,
    // providing it with the LoadParams object and receiving the LoadResult object in return.
    val flow = Pager(
        config = PagingConfig(pageSize = 15),
        pagingSourceFactory = { repository.soundPagingSource(query) }
    ).flow
        .cachedIn(viewModelScope)

    private val counts = repository.observeCount()
    private fun collectCounts() = viewModelScope.launch {
        counts.collect {
            _resultCount.postValue("$it sounds found")
            Log.d("SoundViewModel", "${resultCount.value}")
        }
    }

    init {
        collectCounts()
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