package com.example.soundbrowser.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.soundbrowser.databinding.FragmentSoundItemBinding

class SoundPagingDataAdapter :
    PagingDataAdapter<Sound, SoundItemViewHolder>(DIFFCALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SoundItemViewHolder(
        FragmentSoundItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SoundItemViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(position, item)
        }
    }

    companion object {
        private val DIFFCALLBACK = object : DiffUtil.ItemCallback<Sound>() {
            override fun areItemsTheSame(oldItem: Sound, newItem: Sound): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Sound,
                newItem: Sound
            ): Boolean =
                oldItem == newItem
        }
    }

}