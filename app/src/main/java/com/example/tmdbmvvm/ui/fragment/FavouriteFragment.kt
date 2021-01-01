package com.example.tmdbmvvm.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tmdbmvvm.R
import com.example.tmdbmvvm.databinding.FavouriteFragmentBinding

class FavouriteFragment : Fragment(R.layout.favourite_fragment) {
    private var _binding: FavouriteFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FavouriteFragmentBinding.bind(view)
    }
}