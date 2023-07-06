package com.example.soundbrowser.main

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.soundbrowser.databinding.FragmentSoundListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SoundFragment : Fragment() {

    private lateinit var viewModel: SoundViewModel
    private lateinit var binding: FragmentSoundListBinding
    private lateinit var adapter: SoundPagingDataAdapter
    private lateinit var repository: SoundRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSoundListBinding.inflate(layoutInflater)
        repository = SoundRepository()
        viewModel = ViewModelProvider(
            this,
            SoundViewModelFactory(repository)
        )[SoundViewModel::class.java]

        adapter = SoundPagingDataAdapter()
        binding.soundList.adapter = adapter
        binding.viewModel = viewModel

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }

        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d("SearchView", "Query text submit: $query")
                    viewModel.query = query ?: SoundViewModel.DEFAULT_QUERY
                    adapter.refresh()
                    return false
                }
            })
            queryHint = SoundViewModel.DEFAULT_QUERY
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
        }

        return binding.root
    }

}

