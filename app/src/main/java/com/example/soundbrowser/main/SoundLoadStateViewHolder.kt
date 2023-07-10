package com.example.soundbrowser.main

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.soundbrowser.databinding.FragmentSoundLoadStateBinding

class SoundLoadStateViewHolder(
    private val binding: FragmentSoundLoadStateBinding,
    private val retryCb: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.loadStateRetry.setOnClickListener { retryCb() }
    }

    fun bind(loadState: LoadState) {
        binding.apply {
            loadStateProgress.isVisible = loadState is LoadState.Loading
            loadStateRetry.isVisible = loadState is LoadState.Error
            loadStateError.isVisible = loadState is LoadState.Error
            if (loadState is LoadState.Error) {
                loadStateError.text = loadState.error.message
            }
        }
    }
}