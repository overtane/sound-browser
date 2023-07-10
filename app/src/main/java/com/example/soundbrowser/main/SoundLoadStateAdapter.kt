package com.example.soundbrowser.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.soundbrowser.databinding.FragmentSoundLoadStateBinding

class SoundLoadStateAdapter : LoadStateAdapter<SoundLoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState) =
        SoundLoadStateViewHolder(
            FragmentSoundLoadStateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
    )

    override fun onBindViewHolder(
        holder: SoundLoadStateViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)
}