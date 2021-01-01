package com.example.tmdbmvvm.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.tmdbmvvm.R
import com.example.tmdbmvvm.databinding.SettingFragmentBinding

class SettingFragment : Fragment(R.layout.setting_fragment) {

    private var _binding: SettingFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = SettingFragmentBinding.bind(view)
    }

}