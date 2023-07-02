package com.example.soundbrowser.main

import androidx.recyclerview.widget.RecyclerView
import com.example.soundbrowser.databinding.FragmentSoundItemBinding
import com.example.soundbrowser.sounddb.SoundDbResult

class SoundItemViewHolder(private val binding: FragmentSoundItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(sound: SoundDbResult) {
        binding.apply {
            itemNumber.text = sound.id.toString()
            content.text = sound.name
        }
    }
}