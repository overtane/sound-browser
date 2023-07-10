package com.example.soundbrowser.main

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.soundbrowser.databinding.FragmentSoundLoadStateBinding

class SoundLoadStateViewHolder(
    private val binding: FragmentSoundLoadStateBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.apply {
            loadStateProgress.isVisible = loadState is LoadState.Loading
        }
    }
}