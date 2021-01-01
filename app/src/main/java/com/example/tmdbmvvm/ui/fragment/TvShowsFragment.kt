package com.example.tmdbmvvm.ui.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tmdbmvvm.R
import com.example.tmdbmvvm.databinding.TvshowsFragmentBinding
import com.example.tmdbmvvm.ui.tvShowsFragments.TvAiringTodayFragment
import com.example.tmdbmvvm.ui.tvShowsFragments.TvOnTheAirFragment
import com.example.tmdbmvvm.ui.tvShowsFragments.TvPopularFragment
import com.example.tmdbmvvm.ui.tvShowsFragments.TvTopRatedFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowsFragment : Fragment(R.layout.tvshows_fragment) {

    private var _binding: TvshowsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = TvshowsFragmentBinding.bind(view)

        setUpViewPager()
        setHasOptionsMenu(true)
    }

    private fun setUpViewPager() {
        val viewPager = binding.vpTvShows
        val tab = binding.tlTvShows
        val adapterTv = TvAdapter(this)
        viewPager.adapter = adapterTv

        TabLayoutMediator(tab, viewPager) { tabText, position ->
            tabText.text = when (position) {
                0 -> getString(R.string.title_tvAiringToday)
                1 -> getString(R.string.title_tvOnTheAir)
                2 -> getString(R.string.title_popular)
                3 -> getString(R.string.title_topRated)
                else -> getString(R.string.title_tvAiringToday)

            }
        }.attach()

    }

    private inner class TvAdapter(fm: Fragment) : FragmentStateAdapter(fm) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TvAiringTodayFragment()
                1 -> TvOnTheAirFragment()
                2 -> TvPopularFragment()
                3 -> TvTopRatedFragment()
                else -> TvAiringTodayFragment()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}