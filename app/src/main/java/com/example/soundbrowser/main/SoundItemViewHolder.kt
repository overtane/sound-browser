package com.example.soundbrowser.main

import androidx.recyclerview.widget.RecyclerView
import com.example.soundbrowser.databinding.FragmentSoundItemBinding

class SoundItemViewHolder(private val binding: FragmentSoundItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(position: Int, sound: Sound) {
        val actualPosition = position + 1
        binding.apply {
            this.sound = sound
            itemNumber.text = actualPosition.toString()
        }
    }
}