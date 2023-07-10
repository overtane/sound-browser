package com.example.soundbrowser.main

import android.util.Log
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.soundbrowser.databinding.FragmentSoundLoadStateBinding

class SoundLoadStateViewHolder(
    private val binding: FragmentSoundLoadStateBinding,
    private val retryCb: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    init {
        Log.d("LOADSTATE", "CREATE VIEWHOLDER")
        binding.loadStateRetry.setOnClickListener { retryCb() }
    }

    fun bind(loadState: LoadState) {
        Log.d("LOADSTATE", "BIND VIEWHOLDER")
        binding.apply {
            loadStateProgress.isVisible = loadState is LoadState.Loading
            loadStateRetry.isVisible = loadState is LoadState.Error
            loadStateError.isVisible = loadState is LoadState.Error
            if (loadState is LoadState.Error) {
                loadStateError.text = "ERROR PERKELE"
            }
        }
    }
}