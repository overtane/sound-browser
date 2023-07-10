package com.example.soundbrowser.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.soundbrowser.R
import com.example.soundbrowser.databinding.FragmentSoundDetailsBinding
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                myViewModel.details.collect { details ->
                    if (details != null) {
                        binding.detailsDialog.visibility = View.VISIBLE
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                myViewModel.state.collect { state ->
                    when (state) {
                        SoundDetailsViewModel.PlayState.ERROR -> closeDialog()
                        SoundDetailsViewModel.PlayState.LOADING -> Unit
                        else -> binding.detailsLoadingWheel.visibility = View.GONE
                    }
                    binding.detailsPlayButton.isClickable = when (state) {
                        SoundDetailsViewModel.PlayState.LOADING,
                        SoundDetailsViewModel.PlayState.COMPLETED -> false
                        else -> true
                    }
                     val id = when (state) {
                            SoundDetailsViewModel.PlayState.ERROR,
                            SoundDetailsViewModel.PlayState.LOADING -> R.string.loading
                            SoundDetailsViewModel.PlayState.STOPPED,
                            SoundDetailsViewModel.PlayState.COMPLETED -> R.string.play
                            SoundDetailsViewModel.PlayState.PLAYING -> R.string.pause
                    }
                    binding.detailsPlayButton.text = getString(id)
                    myViewModel.playByState(state)
                }
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        myViewModel.onDestroy()
    }

    private fun closeDialog() {
        val duration = Toast.LENGTH_SHORT
        Toast.makeText(context, LOAD_ERROR, duration).show()
        dismiss()
    }

    companion object {
        private const val LOAD_ERROR = "Cannot load the sound"

        private const val ARG_ID = "id"

        @JvmStatic
        fun newInstance(id: Int) =
            SoundDetailsDialog().apply {
                arguments = Bundle().apply { putInt(ARG_ID, id) }
            }
    }
}