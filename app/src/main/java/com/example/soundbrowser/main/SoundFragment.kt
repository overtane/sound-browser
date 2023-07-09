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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.soundbrowser.databinding.FragmentSoundListBinding
import com.example.soundbrowser.details.SoundDetailsDialog
import kotlinx.coroutines.flow.collectLatest
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

        adapter = SoundPagingDataAdapter(SoundItemClickListener { id ->
            Log.d("SoundItemClickListener", "Clicked item $id")
            val dialog = SoundDetailsDialog.newInstance(id)
            dialog.show(childFragmentManager, "SoundDetailsDialog")
        })

        binding = FragmentSoundListBinding.inflate(layoutInflater).apply {
            viewModel = myViewModel
            lifecycleOwner = viewLifecycleOwner

            soundList.adapter = adapter
            soundList.addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )

            searchView.apply {
                setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean = false

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        Log.d("SearchView", "Query text submit: $query")
                        myViewModel.query = query ?: SoundViewModel.DEFAULT_QUERY
                        adapter.refresh()
                        searchView.clearFocus()
                        return true
                    }
                })
                queryHint = SoundViewModel.DEFAULT_QUERY
                imeOptions = EditorInfo.IME_ACTION_SEARCH
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                myViewModel.flow
                    .collectLatest { pagingData -> adapter.submitData(pagingData) }
            }
        }

        return binding.root
    }

}

