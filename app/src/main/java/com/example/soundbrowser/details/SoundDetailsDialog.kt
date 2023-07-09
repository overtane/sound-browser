package com.example.soundbrowser.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.soundbrowser.databinding.FragmentSoundDetailsBinding

class SoundDetailsDialog : DialogFragment() {

    private lateinit var myViewModel: SoundDetailsViewModel
    private lateinit var binding: FragmentSoundDetailsBinding
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { id = it.getInt(ARG_ID) } ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myViewModel = ViewModelProvider(
            this,
            SoundDetailsViewModelFactory(id)
        )[SoundDetailsViewModel::class.java]

        binding = FragmentSoundDetailsBinding.inflate(layoutInflater).apply {
            viewModel = myViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    companion object {
        private const val ARG_ID = "id"

        @JvmStatic
        fun newInstance(id: Int) =
            SoundDetailsDialog().apply {
                arguments = Bundle().apply { putInt(ARG_ID, id) }
            }
    }
}