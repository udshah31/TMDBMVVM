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
import com.example.tmdbmvvm.databinding.MoviesFragmentBinding
import com.example.tmdbmvvm.ui.movieFragments.PopularFragment
import com.example.tmdbmvvm.ui.movieFragments.TopRatedFragment
import com.example.tmdbmvvm.ui.movieFragments.UpcomingFragment
import com.example.tmdbmvvm.ui.movieFragments.NowPlayingFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(R.layout.movies_fragment) {

    private var _binding: MoviesFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = MoviesFragmentBinding.bind(view)

        setUpViewPager()
        setHasOptionsMenu(true)

    }

    private fun setUpViewPager() {
        val viewPager = binding.vpMovies
        val tab = binding.tlMovies
        val adapter = MoviesAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tab, viewPager) { tabText, position ->
            tabText.text = when (position) {
                0 -> getString(R.string.title_nowPlaying)
                1 -> getString(R.string.title_popular)
                2 -> getString(R.string.title_topRated)
                3 -> getString(R.string.title_upcoming)
                else -> getString(R.string.title_nowPlaying)

            }
        }.attach()
    }

    private inner class MoviesAdapter(fm: Fragment) : FragmentStateAdapter(fm) {

        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> NowPlayingFragment()
                1 -> PopularFragment()
                2 -> TopRatedFragment()
                3 -> UpcomingFragment()
                else -> NowPlayingFragment()
            }

        }


    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_search){

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}