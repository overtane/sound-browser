package com.example.soundbrowser.main

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundbrowser.databinding.FragmentSoundListBinding
import com.example.soundbrowser.details.SoundDetailsDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SoundFragment : Fragment() {

    private lateinit var myViewModel: SoundViewModel
    private lateinit var binding: FragmentSoundListBinding
    private lateinit var adapter: SoundPagingDataAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        myViewModel = SoundViewModel()
        adapter = setupPagingDataAdapter()
        binding = setupViewBinding()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                myViewModel.pageFlow
                    .collectLatest { pagingData -> adapter.submitData(pagingData) }
            }
        }
        return binding.root
    }

    private fun setupPagingDataAdapter() = SoundPagingDataAdapter(
        SoundItemClickListener { id ->
            val dialog = SoundDetailsDialog.newInstance(id)
            dialog.show(childFragmentManager, "SoundDetailsDialog")
        })
        .apply {
            addLoadStateListener { loadState ->
                binding.spinner.isVisible =
                    itemCount == 0 && loadState.refresh is LoadState.Loading
                binding.noConnection.isVisible =
                    itemCount == 0 && loadState.refresh is LoadState.Error
            }
        }

    private fun setupViewBinding() = FragmentSoundListBinding.inflate(layoutInflater)
        .apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = myViewModel

            soundList.adapter =
                adapter.withLoadStateFooter(SoundLoadStateAdapter())

            soundList.addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )

            searchView.apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?) = false

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let {
                            SoundRepository.query.update { query }
                            adapter.refresh()
                            searchView.clearFocus()
                        }
                        return true
                    }
                })
                imeOptions = EditorInfo.IME_ACTION_SEARCH
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            }
        }
}

