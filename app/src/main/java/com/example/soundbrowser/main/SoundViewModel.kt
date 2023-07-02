package com.example.soundbrowser.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.soundbrowser.sounddb.SoundDbResult
import kotlinx.coroutines.flow.Flow

class SoundViewModel(
    private var repository: SoundRepository,
) : ViewModel() {

    // The Pager object calls the load() method from the PagingSource object,
    // providing it with the LoadParams object and receiving the LoadResult object in return.
    val flow: Flow<PagingData<SoundDbResult>> = Pager(
        config = PagingConfig(pageSize = 15),
        pagingSourceFactory = { repository.soundPagingSource("piano") }
    ).flow
        .cachedIn(viewModelScope)

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