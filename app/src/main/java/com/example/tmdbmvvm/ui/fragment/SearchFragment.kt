package com.example.tmdbmvvm.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tmdbmvvm.R
import com.example.tmdbmvvm.databinding.SearchFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {
    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SearchFragmentBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}