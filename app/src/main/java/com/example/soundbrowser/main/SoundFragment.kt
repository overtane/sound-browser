package com.example.soundbrowser.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.soundbrowser.R
import com.example.soundbrowser.databinding.FragmentSoundListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SoundFragment : Fragment(), MenuProvider {

    private lateinit var viewModel: SoundViewModel
    private lateinit var binding: FragmentSoundListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSoundListBinding.inflate(layoutInflater)
        val view = binding.root

        viewModel = ViewModelProvider(
            this,
            SoundViewModelFactory(SoundRepository())
        )[SoundViewModel::class.java]

        (requireActivity() as MenuHost).addMenuProvider(
            this,
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )

        val adapter = SoundPagingDataAdapter()
        view.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
        return view
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
        menuInflater.inflate(R.menu.menu_main, menu)

    override fun onMenuItemSelected(item: MenuItem) : Boolean {
        Log.d("MENU", "ITEM SELECTED")
        return true
    }
}