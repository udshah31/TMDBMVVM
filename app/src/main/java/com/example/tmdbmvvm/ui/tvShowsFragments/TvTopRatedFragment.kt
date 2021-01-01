package com.example.tmdbmvvm.ui.tvShowsFragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tmdbmvvm.R
import com.example.tmdbmvvm.adapter.moviesAdapter.LoadStateAdapter
import com.example.tmdbmvvm.adapter.tvShowsAdapter.TvTopRatedAdapter
import com.example.tmdbmvvm.databinding.TvTopratedFragmentBinding
import com.example.tmdbmvvm.viewmodel.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class TvTopRatedFragment : Fragment(R.layout.tv_toprated_fragment) {
    private var _binding: TvTopratedFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TvShowsViewModel>()

    @Inject
    lateinit var adapter: TvTopRatedAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = TvTopratedFragmentBinding.bind(view)

        binding.apply {
            rvTvTopRated.setHasFixedSize(true)
            rvTvTopRated.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { adapter.retry() },
                footer = LoadStateAdapter { adapter.retry() }
            )
            rvTvTopRated.layoutManager =
                GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)

            adapter.setOnItemClickListener { result ->
                val bundle = Bundle().apply {
                    putParcelable("tvTopRated", result)
                }
                findNavController().navigate(
                    R.id.action_nav_tvShows_to_detailTvTopRatedFragment,
                    bundle
                )
            }

            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadState ->
                binding.swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
            }
        }

        viewModel.tvTopRated.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}