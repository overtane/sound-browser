package com.example.soundbrowser.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.soundbrowser.R
import com.example.soundbrowser.placeholder.PlaceholderContent

class SoundFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sound_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
                view.adapter = SoundRecyclerViewAdapter(PlaceholderContent.ITEMS)
        }
        return view
    }
}