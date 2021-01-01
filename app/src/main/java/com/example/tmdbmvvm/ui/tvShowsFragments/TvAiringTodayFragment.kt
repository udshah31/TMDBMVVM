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
import com.example.tmdbmvvm.adapter.tvShowsAdapter.TvAiringTodayAdapter
import com.example.tmdbmvvm.databinding.TvAiringtodayFragmentBinding
import com.example.tmdbmvvm.viewmodel.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class TvAiringTodayFragment : Fragment(R.layout.tv_airingtoday_fragment) {

    private var _binding: TvAiringtodayFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TvShowsViewModel>()

    @Inject
    lateinit var adapter: TvAiringTodayAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = TvAiringtodayFragmentBinding.bind(view)

        binding.apply {
            rvTvAiringToday.setHasFixedSize(true)
            rvTvAiringToday.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadStateAdapter { adapter.retry() },
                footer = LoadStateAdapter { adapter.retry() }
            )
            rvTvAiringToday.layoutManager =
                GridLayoutManager(activity, 3, LinearLayoutManager.VERTICAL, false)

            adapter.setOnItemClickListener { result ->
                val bundle = Bundle().apply {
                    putParcelable("tvAiringToday", result)
                }
                findNavController().navigate(
                    R.id.action_nav_tvShows_to_detailTvAiringTodayFragment,
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

        viewModel.tvAiringToday.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}