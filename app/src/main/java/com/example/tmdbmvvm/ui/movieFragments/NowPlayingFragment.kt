package com.example.tmdbmvvm.ui.movieFragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmvvm.R
import com.example.tmdbmvvm.adapter.moviesAdapter.LoadStateAdapter
import com.example.tmdbmvvm.adapter.moviesAdapter.NowPlayingAdapter
import com.example.tmdbmvvm.databinding.NowplayingFragmentBinding
import com.example.tmdbmvvm.viewmodel.MoviesViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class NowPlayingFragment @Inject constructor() : Fragment(R.layout.nowplaying_fragment) {

    private val viewModel by viewModels<MoviesViewModel>()
    private var _binding: NowplayingFragmentBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var adapter: NowPlayingAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = NowplayingFragmentBinding.bind(view)

        binding.apply {
            rvNowPlaying.setHasFixedSize(true)
            rvNowPlaying.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { adapter.retry() },
                footer = LoadStateAdapter { adapter.retry() }
            )

            rvNowPlaying.layoutManager =
                GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)

            adapter.setOnItemClickListener { result ->
                val bundle = Bundle().apply {
                    putParcelable("movie", result)
                }
                findNavController().navigate(R.id.action_nav_movie_to_detailFragment, bundle)
            }

            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
                binding.progressBar.visibility = View.GONE
            }
        }

        adapter.addLoadStateListener { loadState ->
            binding.rvNowPlaying.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error
            binding.tvErrorMessage.isVisible = loadState.source.refresh is LoadState.Error
            binding.swipeRefresh.isVisible = loadState.source.refresh is LoadState.NotLoading

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Snackbar.make(
                    requireView(),
                    "No Internet : ${errorState.error}",
                    Snackbar.LENGTH_LONG
                ).show()

                binding.tvErrorMessage.text = errorState.error.message
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadState ->
                binding.swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
            }
        }

        viewModel.nowPlaying.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        binding.retryButton.setOnClickListener {
            adapter.retry()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}